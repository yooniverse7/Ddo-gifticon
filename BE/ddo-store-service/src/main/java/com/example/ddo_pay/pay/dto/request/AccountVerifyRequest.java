package com.example.ddo_pay.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 사용자가 계좌 등록 시 작성한 계좌를 담아서 보냄

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerifyRequest {
    private Long userId;
    private String accountNo;
}
