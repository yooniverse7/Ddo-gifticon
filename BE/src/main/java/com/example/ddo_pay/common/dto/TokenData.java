package com.example.ddo_pay.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenData {
    private Long giftId;
    private Long userId;
    private Integer amount;

    public TokenData(Long giftId, Long userId, Integer amount) {
        this.giftId = giftId;
        this.userId = userId;
        this.amount = amount;
    }

    // getter, setter 생략 (Lombok @Data 등을 사용할 수도 있음)
}
