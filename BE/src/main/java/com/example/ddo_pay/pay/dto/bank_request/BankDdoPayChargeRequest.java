package com.example.ddo_pay.pay.dto.bank_request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 은행 서버에 계좌이체 요청

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDdoPayChargeRequest {
    @JsonProperty("userAccountNum")
    private String userAccountNum; // 사용자 계좌(또페이 충전) or 법인 계좌(기프티콘 결제)
    @JsonProperty("corporationAccountNum")
    private String corporationAccountNum; // 법인 계좌(또페이 충전) or 가게 계좌(기프티콘 결제)

    private int amount;

}
