package com.example.ddo_pay.gift.dto.create;

import lombok.Data;

@Data
public class GiftCreateRequestDto {

    private String giftTitle;
    private int amount;
    private String phoneNum;
    private String message;
    private String image;

    private int userId;
}
