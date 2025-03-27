package com.example.ddo_pay.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    // 로그인 정보
    private long userId;
    private String loginId;
    private String password;
    private String refreshToken;

    // 개인 정보
    private String name;
    private String email;
    private String phoneNum;
    private String birth;

}
