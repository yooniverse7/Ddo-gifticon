package com.example.ddo_pay.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.ddo_pay.common.util.JwtUtil;
import com.example.ddo_pay.user.dto.response.SocialLoginResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.ddo_pay.user.dto.response.KakaoTokenResponse;
import com.example.ddo_pay.user.dto.response.KakaoUserResponse;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.service.impl.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final JwtUtil jwtUtil;

	@Value("${kakao.rest-api-key}")
	private String restApiKey;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Value("${kakao.client-secret:}") // 없을 수도 있으므로 default 빈문자
	private String clientSecret;

	/**
	 * 카카오 인가 코드를 받아 로그인 처리
	 *
	 * @param authorizationCode 프론트에서 받은 인가 코드
	 * @return 로그인된 사용자 엔티티
	 */
	public SocialLoginResponseDto loginWithKakao(String authorizationCode) {
		// 1) 인가 코드로 액세스 토큰 요청
		KakaoTokenResponse tokenResponse = getToken(authorizationCode);
		String accessToken = tokenResponse.getAccessToken();
		String kakaoRefreshToken = tokenResponse.getRefreshToken(); // 카카오 리프레시 토큰

		// 2) 액세스 토큰으로 카카오 사용자 정보 요청
		KakaoUserResponse userResponse = getUserInfo(accessToken);

		// 3) 필요 정보 파싱 (null 체크 추가)
		Long kakaoId = userResponse.getId();
		KakaoUserResponse.KakaoAccount account = userResponse.getKakaoAccount();

		// 각 필드를 안전하게 초기화 (final 변수로 선언)
		final String finalName;
		final String email;
		final String phone;
		final LocalDateTime birthdayDate;

		if (account != null) {
			// 이름: account.getName()이 있으면 사용, 없으면 profile.nickname, 모두 없으면 "Unknown"
			if (account.getName() != null) {
				finalName = account.getName();
			} else if (account.getProfile() != null && account.getProfile().getNickname() != null) {
				finalName = account.getProfile().getNickname();
			} else {
				finalName = "Unknown";
			}
			email = (account.getEmail() != null) ? account.getEmail() : "";
			phone = (account.getPhoneNumber() != null) ? account.getPhoneNumber() : "";
			if (account.getBirthyear() != null && account.getBirthday() != null) {
				birthdayDate = convertToBirthDate(account.getBirthyear(), account.getBirthday());
			} else {
				birthdayDate = null;
			}
		} else {
			// account가 null인 경우 기본값 설정
			finalName = "Unknown";
			email = "";
			phone = "";
			birthdayDate = null;
		}

		// 4) DB에서 해당 kakaoId 사용자가 존재하는지 확인 후 업데이트 또는 신규 생성
		Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
		User user = optionalUser
				.map(existingUser -> {
					User updatedUser = existingUser.toBuilder()
							.name(finalName)
							.email(email)
							.phoneNum(phone)
							.birthday(birthdayDate != null ? birthdayDate : existingUser.getBirthday())
							.refreshToken(kakaoRefreshToken)  // 카카오 리프레시 토큰 저장
							.build();
					userRepository.save(updatedUser);
					return updatedUser;
				})
				.orElseGet(() -> {
					User newUser = User.builder()
							.loginId(null)  // 카카오 로그인 시 별도의 loginId 필요 없음
							.name(finalName)
							.email(email)
							.phoneNum(phone)
							.birthday(birthdayDate)
							.kakaoId(kakaoId)
							.refreshToken(kakaoRefreshToken)  // 신규 사용자에도 카카오 리프레시 토큰 저장
							.build();
					userRepository.save(newUser);
					return newUser;
				});

		// 5) JWT 토큰 발급 (예: 사용자 정보를 기반으로 액세스, 리프레시 토큰 생성)
		String jwtAccessToken = jwtUtil.generateAccessToken(user);
		String jwtRefreshToken = jwtUtil.generateRefreshToken(user);

		// 6) SocialLoginResponseDto에 JWT 토큰 세팅 후 반환
		SocialLoginResponseDto responseDto = new SocialLoginResponseDto();
		responseDto.setAccessToken(jwtAccessToken);
		responseDto.setRefreshToken(jwtRefreshToken);
		responseDto.setName(user.getName());
		return responseDto;
	}

	/**
	 * 인가 코드로 카카오 토큰 발급
	 */
	private KakaoTokenResponse getToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", restApiKey);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);
		if (clientSecret != null && !clientSecret.isEmpty()) {
			params.add("client_secret", clientSecret);
		}

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
				"https://kauth.kakao.com/oauth/token",
				request,
				KakaoTokenResponse.class
		);

		KakaoTokenResponse tokenResponse = response.getBody();
		System.out.println("Kakao Token Response: " + tokenResponse); // 디버깅 로그
		return tokenResponse;
	}

	/**
	 * 액세스 토큰으로 카카오 사용자 정보 가져오기
	 */
	private KakaoUserResponse getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken); // Authorization: Bearer {accessToken}

		HttpEntity<Void> request = new HttpEntity<>(headers);
		ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.GET,
				request,
				KakaoUserResponse.class
		);
		return response.getBody();
	}

	/**
	 * birthyear(YYYY)와 birthday(MMDD)를 LocalDateTime으로 변환
	 */
	private LocalDateTime convertToBirthDate(String birthyear, String birthday) {
		// 예: birthyear="2000", birthday="0115" -> "2000-01-15" 파싱 후 00:00:00 시각
		String birthString = birthyear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2);
		LocalDate localDate = LocalDate.parse(birthString);
		return localDate.atStartOfDay();
	}
}