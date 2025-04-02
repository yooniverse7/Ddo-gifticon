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


// 계좌 내역 연결
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectHistoryRequestDto {

    @Builder.Default
    @JsonProperty("Header")
    private Map<String, Object> Header = new HashMap<>();

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("orderByType")
    private String orderByType;

    public static SelectHistoryRequestDto of(String apiKey, String userKey, String accountNo) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String institutionTransactionUniqueNo = date + time + "123453";

        LocalDateTime oneMonthAgo = now.minusMonths(1);
        String startDate = oneMonthAgo.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Map<String, Object> header = new HashMap<>();
        header.put("apiName", "inquireTransactionHistoryList");
        header.put("transmissionDate", date);
        header.put("transmissionTime", time);
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "inquireTransactionHistoryList");
        header.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        return SelectHistoryRequestDto.builder()
                .Header(header)
                .accountNo(accountNo)
                .startDate(startDate)
                .endDate(date)
                .transactionType("A")
                .orderByType("ASC")
                .build();

    }

}
