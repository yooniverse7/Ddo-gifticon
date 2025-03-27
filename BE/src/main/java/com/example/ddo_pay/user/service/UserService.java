package com.example.ddo_pay.user.service;

import com.example.ddo_pay.user.dto.UserDto;
import com.example.ddo_pay.user.dto.request.SocialLoginRequestDto;
import com.example.ddo_pay.user.dto.response.SocialLoginResponseDto;

public interface UserService {
    /**
     * 소셜 로그인을 진행한다.
     * 웬만하면 건드리지 않기
     * 
     * @param
     * {@code SocialLoginRequestDto} reqDto
     * @return SocialLoginResponseDto
     */
    SocialLoginResponseDto socialUserLogin(SocialLoginRequestDto reqDto);

    /**
     * userId 로 유저 정보를 가져온다.<br>
     * 
     * @param reqDto userId 필수
     * @return UserDto
     */
    UserDto getUserInfo(UserDto reqDto);

    /**
     * 유저 정보를 변경한다.<br>
     * 
     * @param reqDto userId 필수
     */
    void changeUserInfo(UserDto reqDto);

    /**
     * 로그아웃을 진행한다.<br>
     * 
     * @param reqDto userId 필수
     */
    void logoutUser(UserDto reqDto);
}