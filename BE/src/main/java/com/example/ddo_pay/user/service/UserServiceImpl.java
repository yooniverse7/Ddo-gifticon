package com.example.ddo_pay.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ddo_pay.user.dto.KakaoUserInfo;
import com.example.ddo_pay.user.dto.UserDto;
import com.example.ddo_pay.user.dto.request.SocialLoginRequestDto;
import com.example.ddo_pay.user.dto.response.KakaoTokenResponse;
import com.example.ddo_pay.user.dto.response.SocialLoginResponseDto;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.mapper.UserMapper;
import com.example.ddo_pay.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public SocialLoginResponseDto socialUserLogin(SocialLoginRequestDto reqDto) {
        // 1. 프론트엔드에서 전달받은 인가 코드(code) 사용
        String code = reqDto.getCode();

        // 2. 카카오 API를 통해 토큰 발급
        KakaoTokenResponse tokenResponse = kakaoAuthService.getKakaoToken(code);
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("카카오 토큰 발급 실패");
        }

        // 3. 액세스 토큰으로 카카오 사용자 정보 조회
        KakaoUserInfo kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(tokenResponse.getAccessToken());
        if (kakaoUserInfo == null) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패");
        }

        // 4. DB에서 사용자 존재 여부 확인 (카카오 고유 ID를 문자열로 저장한다고 가정)
        Optional<User> userOptional = userRepo.findByLoginId(String.valueOf(kakaoUserInfo.getId()));
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            log.debug("기존 사용자 발견: " + user.getName());
            // 필요에 따라 사용자 정보를 업데이트 할 수 있음
        } else {
            // 신규 회원 등록
            user = User.builder()
                .loginId(String.valueOf(kakaoUserInfo.getId()))
                .name(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                // 추가 필드 설정
                .build();
            userRepo.save(user);
        }

        // 5. 응답 생성: 실제 서비스에서는 자체 JWT 발급 로직을 추가할 수 있음
        SocialLoginResponseDto respDto = new SocialLoginResponseDto();
        respDto.setAccessToken(tokenResponse.getAccessToken());
        respDto.setRefreshToken(tokenResponse.getRefreshToken());

        return respDto;
    }

    @Override
    public UserDto getUserInfo(UserDto reqDto) {
        Optional<User> targetUser = userRepo.findById(reqDto.getUserId());
        if (targetUser.isEmpty()) {
            log.info("Fail GetUser. No UserId in DB");
            // 필요시 예외 처리
        }
        UserDto respDto = userMapper.fromUserEntity(targetUser.get());
        return respDto;
    }

    @Override
    public void changeUserInfo(UserDto reqDto) {
        Optional<User> targetUser = userRepo.findById(reqDto.getUserId());
        if (targetUser.isEmpty()) {
            log.info("Fail GetUser. No UserId in DB");
            // 필요시 예외 처리
        }
        User user = targetUser.get();
        user.changePrivateInfo(reqDto);
        userRepo.save(user);
    }

    @Override
    public void logoutUser(UserDto reqDto) {
        // 로그아웃 처리 (예: 세션 무효화, JWT 블랙리스트 등록 등)
    }
}
