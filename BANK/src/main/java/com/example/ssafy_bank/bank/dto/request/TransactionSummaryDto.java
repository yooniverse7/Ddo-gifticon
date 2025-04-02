package com.example.ssafy_bank.bank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

// 계좌 내역 반환
@Data
@AllArgsConstructor
public class TransactionSummaryDto {
    private String transactionTypeName;
    private String transactionAfterBalance;
    private String transactionDate;
    private String transactionTime;
}
