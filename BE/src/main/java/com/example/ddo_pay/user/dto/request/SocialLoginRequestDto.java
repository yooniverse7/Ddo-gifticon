package com.example.ddo_pay.user.dto.request;

import lombok.Data;

@Data
public class SocialLoginRequestDto {
    private String socialId;
    // 이제 인가 코드를 받을 필드 추가
    private String code;
}
