package com.ddopay.bankservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankVerifyRequest {
    private String accountNumber;
    private String userId;
}
