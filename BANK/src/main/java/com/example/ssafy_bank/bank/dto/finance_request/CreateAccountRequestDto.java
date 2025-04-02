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

// 계좌 생성
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequestDto {

    @Builder.Default
    @JsonProperty("Header")
    private Map<String, Object> Header = new HashMap<>();

    @JsonProperty("accountTypeUniqueNo")
    private String accountTypeUniqueNo;

    public static CreateAccountRequestDto of(String userKey, String apiKey) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String institutionTransactionUniqueNo = date + time + "123453";

        Map<String, Object> header = new HashMap<>();
        header.put("apiName", "createDemandDepositAccount");
        header.put("transmissionDate", date);
        header.put("transmissionTime", time);
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "createDemandDepositAccount");
        header.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        return CreateAccountRequestDto.builder()
                .Header(header)
                .accountTypeUniqueNo("999-1-6df03694657b40")
                .build();

    }

}
