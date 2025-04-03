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
    private String userAccountNum;
    @JsonProperty("corporationAccountNum")
    private String corporationAccountNum;

    private int amount;

}
