package com.example.ddo_pay.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.example.ddo_pay.user.dto.KakaoUserInfo;
import com.example.ddo_pay.user.dto.response.KakaoTokenResponse;

@Service
public class KakaoAuthService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${kakao.rest-api-key}")
	private String kakaoRestApiKey;

	@Value("${kakao.redirect-uri}")
	private String kakaoRedirectUri;

	// client_secret이 필요하면 properties에 설정하고 사용
	@Value("${kakao.client-secret:}")
	private String kakaoClientSecret;

	// 카카오 토큰 엔드포인트 호출
	public KakaoTokenResponse getKakaoToken(String code) {
		String url = "https://kauth.kakao.com/oauth/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoRestApiKey);
		params.add("redirect_uri", kakaoRedirectUri);
		params.add("code", code);
		if (StringUtils.hasText(kakaoClientSecret)) {
			params.add("client_secret", kakaoClientSecret);
		}

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
			url,
			HttpMethod.POST,
			requestEntity,
			KakaoTokenResponse.class
		);

		return response.getBody();
	}

	// 카카오 사용자 정보 엔드포인트 호출
	public KakaoUserInfo getKakaoUserInfo(String accessToken) {
		String url = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);  // Authorization: Bearer {accessToken}
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
			url,
			HttpMethod.GET,
			requestEntity,
			KakaoUserInfo.class
		);

		return response.getBody();
	}
}

