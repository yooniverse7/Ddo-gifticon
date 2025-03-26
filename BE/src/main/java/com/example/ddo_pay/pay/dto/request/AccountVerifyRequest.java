package com.example.ddo_pay.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerifyRequest {
    private Long userId;
    private String accountNo;
}
