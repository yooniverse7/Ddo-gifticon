package com.example.ddo_pay.pay.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// 계좌 인증 시 레디스 저장 dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDto {
    private Long userId; // 사용자 id
    private String word; // 랜덤 단어
    private String accountNo; // 계좌번호
}
