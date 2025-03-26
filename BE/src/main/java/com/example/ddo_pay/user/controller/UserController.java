package com.example.ddo_pay.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ddo_pay.common.response.Response;
import static com.example.ddo_pay.common.response.ResponseCode.SUCCESS_GET_USER_INFO;
import static com.example.ddo_pay.common.response.ResponseCode.SUCCESS_LOGOUT;
import static com.example.ddo_pay.common.response.ResponseCode.SUCCESS_SOCIAL_LOGIN;
import static com.example.ddo_pay.common.response.ResponseCode.SUCCESS_UPDATE_USER_INFO;
import com.example.ddo_pay.common.util.SecurityUtil;
import com.example.ddo_pay.user.dto.UserDto;
import com.example.ddo_pay.user.dto.request.SocialLoginRequestDto;
import com.example.ddo_pay.user.dto.request.UserInfoRequestDto;
import com.example.ddo_pay.user.dto.response.SocialLoginResponseDto;
import com.example.ddo_pay.user.dto.response.UserInfoResponseDto;
import com.example.ddo_pay.user.mapper.UserMapper;
import com.example.ddo_pay.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    // 소셜 로그인
    // Request - SocialLoginRequestDto
    // Response - SocialLoginResponseDto
    @PostMapping("/social/kakao/login")
    public ResponseEntity<?> socialUserLogin(@RequestBody SocialLoginRequestDto reqDto) {
        SocialLoginResponseDto socialLoginDto = userService.socialUserLogin(reqDto);
        return new ResponseEntity<>(Response.create(SUCCESS_SOCIAL_LOGIN, socialLoginDto),
                SUCCESS_SOCIAL_LOGIN.getHttpStatus());
    }

    // 회원 정보 불러오기
    // Request - null
    // Response - UserInfoResponseDto
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {

        log.info("userId : " + SecurityUtil.getUserId());

        UserDto userDto = userService.getUserInfo(UserDto.builder()
                .userId(SecurityUtil.getUserId())
                .build());
        UserInfoResponseDto respDto = userMapper.toUserInfoResponseDto(userDto);

        return new ResponseEntity<>(Response.create(SUCCESS_GET_USER_INFO, respDto),
                SUCCESS_GET_USER_INFO.getHttpStatus());
    }

    // 회원 정보 수정
    // Request - UserInfoRequestDto
    // Response - null
    @PutMapping("/info")
    public ResponseEntity<?> changeUserInfo(@RequestBody UserInfoRequestDto infoDto) {
        UserDto userDto = userMapper.fromUserInfoRequestDto(infoDto);
        userDto.setUserId(SecurityUtil.getUserId());
        userService.changeUserInfo(userDto);

        return new ResponseEntity<>(Response.create(SUCCESS_UPDATE_USER_INFO, null),
                SUCCESS_UPDATE_USER_INFO.getHttpStatus());
    }

    // 로그아웃
    // Request - null
    // Response - null
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        userService.logoutUser(UserDto.builder()
                .userId(SecurityUtil.getUserId())
                .build());

        return new ResponseEntity<>(Response.create(SUCCESS_LOGOUT, null),
                SUCCESS_LOGOUT.getHttpStatus());
    }
}
