package com.example.ddo_pay.pay.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBalanceResponse {

    @JsonIgnore
    private Long userId;
    private int payBalance;

    public GetBalanceResponse(int balance) {
        this.payBalance = balance;
    }
}
