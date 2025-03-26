package com.example.ddo_pay.gift.dto.select;

import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.USED;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GiftDetailResponseDto {

    private String giftTitle;
    private int amount;
    private String phoneNum;
    private String message;
    private String image;
    private LocalDateTime period;
    private USED usedStatus;
    private Long restaurantId;
    private String restaurantName;


    public static GiftDetailResponseDto from(Gift gift) {
        GiftDetailResponseDto dto = new GiftDetailResponseDto();
        dto.giftTitle = gift.getTitle();
        dto.amount = gift.getAmount();
        dto.phoneNum = gift.getPhoneNum();
        dto.message = gift.getMessage();
        dto.image = gift.getImage();
        dto.period = gift.getPeriod();
        dto.usedStatus = gift.getUsedStatus();
        dto.restaurantId = gift.getRestaurant().getId();
        dto.restaurantName = gift.getRestaurant().getName();

        return dto;
    }
}
