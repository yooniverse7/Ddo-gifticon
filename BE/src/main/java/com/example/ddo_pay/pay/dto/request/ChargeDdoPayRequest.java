package com.example.ddo_pay.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeDdoPayRequest {
    private Long userId;
    private int amount;
    private String password;
}
