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

// 계좌 잔액 조회
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectBalanceRequestDto {

    @Builder.Default
    @JsonProperty("Header")
    private Map<String, Object> Header = new HashMap<>();

    @JsonProperty("accountNo")
    private String accountNo;


    public static SelectBalanceRequestDto of(String apiKey, String userKey, String accountNo) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String institutionTransactionUniqueNo = date + time + "123453";

        Map<String, Object> header = new HashMap<>();
        header.put("apiName", "inquireDemandDepositAccountBalance");
        header.put("transmissionDate", date);
        header.put("transmissionTime", time);
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "inquireDemandDepositAccountBalance");
        header.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);



        return SelectBalanceRequestDto.builder()
                .Header(header)
                .accountNo(accountNo)
                .build();

    }
}
