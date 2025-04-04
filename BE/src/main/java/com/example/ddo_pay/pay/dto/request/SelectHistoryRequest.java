package com.example.ddo_pay.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 결제 내역 조회
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectHistoryRequest {
    private String historyType;
}
