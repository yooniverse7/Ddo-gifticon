package com.example.ddo_pay.pay.dto.finance;

import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


// 계좌이체 요청 dto

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositAccountWithdrawRequestDto {

    @Builder.Default
    private Map<String, Object> Header = new HashMap<>();
    private String depositAccountNo;
    private String depositTransactionSummary;
    private String transactionBalance;
    private String withdrawalAccountNo;
    private String withdrawalTransactionSummary;

    public static DepositAccountWithdrawRequestDto of(String depositAccountNo, String memo) {
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
        header.put("apiKey", "bcc132cc5c0e43fc9cf9ffb61369c224"); // 고정 키
        header.put("userKey", "f6c4be2f-823e-4341-83aa-bd19240c564f"); // 법인 userKey

        return DepositAccountWithdrawRequestDto.builder()
                .Header(header)
                .depositAccountNo(depositAccountNo)
                .depositTransactionSummary("(수시입출금) : 입금(" + memo + ")")
                .transactionBalance("1")
                .withdrawalAccountNo("9990627419918613") // 법인 계좌
                .withdrawalTransactionSummary("(수시입출금) : 출금(이체)")
                .build();
    }

}
