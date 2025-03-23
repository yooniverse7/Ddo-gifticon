package com.example.ddo_pay.pay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAccountResponse {
    private int accountId; // 등록 계좌 아이디
    private String accountBank; // 등록 계좌 은행
    private String accountNumber; // 등록 계좌 번호
}
