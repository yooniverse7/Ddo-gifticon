package com.example.ssafy_bank.bank.dto.finance_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SelectHistoryResponseDto {

    @JsonProperty("Header")
    private Header header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    public static class Header {
        private String responseCode;
        private String responseMessage;
        // 기타 필요 필드...
    }

    @Data
    public static class Rec {
        private String totalCount;
        private List<TransactionDetail> list;
    }

    @Data
    public static class TransactionDetail {
        private String transactionUniqueNo;
        private String transactionDate;
        private String transactionTime;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
        private String transactionBalance;
        private String transactionAfterBalance;
        private String transactionSummary;
        private String transactionMemo;
    }
}