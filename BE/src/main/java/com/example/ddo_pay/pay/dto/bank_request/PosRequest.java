package com.example.ddo_pay.pay.dto.bank_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// pos에서 받아오기
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PosRequest {

    @JsonProperty("storeAccount")
    private String storeAccount; // 가게 계좌
    @JsonProperty("paymentToken")
    private String paymentToken; // 토큰
    @JsonProperty("paymentAmount")
    private int paymentAmount; // 결제 금액
}
