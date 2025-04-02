package com.example.ssafy_bank.bank.dto.finance_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SelectBalanceResponseDto {

    @JsonProperty("REC")
    private Rec rec;

    @Data
    public static class Rec {
        private String bankCode;
        private String accountNo;
        // 여기서 accountBalance 값만 필요함 (문자열로 받는다고 가정)
        private String accountBalance;
        // 필요하면 다른 필드도 추가 가능 (accountCreatedDate, etc.)
    }
}
