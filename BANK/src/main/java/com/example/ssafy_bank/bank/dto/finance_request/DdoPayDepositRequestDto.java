package com.example.ssafy_bank.bank.dto.finance_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

// 또페이 충전을 위한 계좌이체 요청 dto
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DdoPayDepositRequestDto {


    @Builder.Default
    @JsonProperty("Header")
    private Map<String, Object> Header = new HashMap<>();

    @JsonProperty("depositAccountNo")
    private String depositAccountNo;

    @JsonProperty("depositTransactionSummary")
    private String depositTransactionSummary;

    @JsonProperty("transactionBalance")
    private String transactionBalance;

    @JsonProperty("withdrawalAccountNo")
    private String withdrawalAccountNo;

    @JsonProperty("withdrawalTransactionSummary")
    private String withdrawalTransactionSummary;

    public static DdoPayDepositRequestDto of(String userAccountNum, String apiKey, String corporationAccountNum, int amount, String userKey) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String institutionTransactionUniqueNo = date + time + "123453";

        Map<String, Object> header = new HashMap<>();
        header.put("apiName", "updateDemandDepositAccountTransfer");
        header.put("transmissionDate", date);
        header.put("transmissionTime", time);
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "updateDemandDepositAccountTransfer");
        header.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        return DdoPayDepositRequestDto.builder()
                .Header(header)
                .depositAccountNo(corporationAccountNum)
                .depositTransactionSummary("(수시입출금) : 입금")
                .transactionBalance("1")
                .withdrawalAccountNo(userAccountNum)
                .withdrawalTransactionSummary("(수시입출금) : 출금(이체)")
                .build();
    }

}
