package com.example.ddo_pay.user.controller;

import com.example.ddo_pay.user.dto.request.SocialLoginRequestDto;
import com.example.ddo_pay.user.dto.response.SocialLoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	/**
	 * 카카오 인가 코드 받아 로그인 처리
	 * (GET 예시, POST로 해도 됨)
	 */
	@PostMapping("/kakao/callback")
	public ResponseEntity<?> kakaoCallback(@RequestBody SocialLoginRequestDto reqDto) {
		// 1) 카카오 서비스 호출하여 로그인 처리
//		User user = kakaoAuthService.loginWithKakao(reqDto.getCode());
//
//		// 2) DB의 유저 정보나 JWT 토큰 발급 결과를 반환
//		// 여기서는 예시로 user 엔티티를 직접 반환
//		// 실제로는 필요 데이터만 담아 DTO로 반환하거나,
//		// 프론트엔드로 리다이렉트하는 방식 쓰기도 함
//		return ResponseEntity.ok(user);
		SocialLoginResponseDto responseDto = kakaoAuthService.loginWithKakao(reqDto.getCode());
		return ResponseEntity.ok(responseDto);
	}
}
