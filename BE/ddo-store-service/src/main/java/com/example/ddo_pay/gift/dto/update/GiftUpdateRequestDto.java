package com.example.ddo_pay.gift.dto.update;

import lombok.Data;

@Data
public class GiftUpdateRequestDto {
    private int giftId;
    private String phoneNum;

    private int userId;
}

