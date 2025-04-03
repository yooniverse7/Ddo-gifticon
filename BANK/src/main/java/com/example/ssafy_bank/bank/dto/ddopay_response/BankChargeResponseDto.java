package com.example.ssafy_bank.bank.dto.ddopay_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankChargeResponseDto {

    @JsonProperty("Header")
    private BankChargeHeader header;

    @JsonProperty("REC")
    private List<TransactionRecord> rec;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BankChargeHeader {
        private String responseCode;
        private String responseMessage;
        private String apiName;
        private String transmissionDate;
        private String transmissionTime;
        private String institutionCode;
        private String apiKey;
        private String apiServiceCode;
        private String institutionTransactionUniqueNo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TransactionRecord {
        private String transactionUniqueNo;
        private String accountNo;
        private String transactionDate;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
    }
}
