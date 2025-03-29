package com.example.ddo_pay.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// 계좌 인증 후 랜덤 단어 확인 요청

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RandomWordRequest {
    private Long userId;
    private String randomWord;
}