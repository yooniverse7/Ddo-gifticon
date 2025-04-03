package com.example.ssafy_bank.bank.dto.ddopay_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// 또페이 충전 시 받아오는 dto
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeDdoPayRequestDto {
    private String userAccountNum;
    private String corporationAccountNum;
    private int amount;
}
