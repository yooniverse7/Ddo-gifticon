package com.example.ddo_pay.gift.dto.select;

import lombok.Data;

@Data
public class GiftCheckRequestDto {
    private String latitude;
    private String longitude;
    private int giftId;

    private int userId;
}
