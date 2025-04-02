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

// 계좌 생성 후 1000만원 입금 요청

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Deposit1000RequestDto {

    @Builder.Default
    @JsonProperty("Header")
    private Map<String, Object> Header = new HashMap<>();

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("transactionBalance")
    private String transactionBalance;

    @JsonProperty("transactionSummary")
    private String transactionSummary;

   public static Deposit1000RequestDto of(String accountNo, String apiKey) {
       LocalDateTime now = LocalDateTime.now();
       String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
       String time = now.format(DateTimeFormatter.ofPattern("HHmmss"));
       String institutionTransactionUniqueNo = date + time + "123453";

       Map<String, Object> header = new HashMap<>();
       header.put("apiName", "updateDemandDepositAccountDeposit");
       header.put("transmissionDate", date);
       header.put("transmissionTime", time);
       header.put("institutionCode", "00100");
       header.put("fintechAppNo", "001");
       header.put("apiServiceCode", "updateDemandDepositAccountDeposit");
       header.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
       header.put("apiKey", apiKey);
       header.put("userKey", "f99b130c-a37e-4ab2-92ca-d9aaa8329bcc");



       return Deposit1000RequestDto.builder()
               .Header(header)
               .accountNo(accountNo)
               .transactionBalance("10000000")
               .transactionSummary("수시입출금) : 입금")
               .build();


   }

}
