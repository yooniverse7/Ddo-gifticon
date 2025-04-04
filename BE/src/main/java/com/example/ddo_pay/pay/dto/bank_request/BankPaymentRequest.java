package com.example.ddo_pay.pay.dto.bank_request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankPaymentRequest {

    private String storeAccountNum; // .가게 계좌 ( 입금받는 계좌)
    private String corporationAccountNum; // 법인 계좌
    private int amount; // 결제 금액
}
