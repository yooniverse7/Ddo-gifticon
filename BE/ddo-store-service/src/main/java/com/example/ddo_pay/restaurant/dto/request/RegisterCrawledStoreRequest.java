package com.example.ddo_pay.restaurant.dto.request;

import lombok.Data;

@Data
public class RegisterCrawledStoreRequest {
    private String cacheKey;   // Redis 키
    private int storeIndex;    // 선택 매장 인덱스
    private Long userId;       // 등록 유저
}
