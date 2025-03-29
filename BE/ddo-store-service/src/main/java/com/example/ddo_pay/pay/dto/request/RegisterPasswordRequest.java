package com.example.ddo_pay.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입 후 결제 비밀번호 등록 시,

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterPasswordRequest {
    private Long userId;
    private String password; // 6자리 숫자
}
