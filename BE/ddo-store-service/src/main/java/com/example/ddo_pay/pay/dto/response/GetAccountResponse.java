package com.example.ddo_pay.pay.dto.response;

import com.example.ddo_pay.pay.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAccountResponse {
    @JsonIgnore
    private Long userId;
    private Long accountId; // 등록 계좌 아이디
    private String accountNumber; // 등록 계좌 번호

    public static GetAccountResponse from(Account account) {
        GetAccountResponse response = new GetAccountResponse();
        response.accountId = account.getId();
        response.accountNumber = account.getAccountNum();
        return response;
    }
}
