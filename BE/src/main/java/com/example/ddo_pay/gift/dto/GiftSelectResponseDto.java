package com.example.ddo_pay.gift.dto;

import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.USED;
import lombok.Data;

@Data
public class GiftSelectResponseDto {
    private Long giftId;
    private String sendUserName;
    private String period;
    private String image;
    private USED isUsed;

    public static GiftSelectResponseDto from(Gift gift) {
        GiftSelectResponseDto dto = new GiftSelectResponseDto();
        dto.giftId = gift.getId();
        dto.sendUserName = gift.getUser().getName();
        dto.period = gift.getPeriod().toString();
        dto.image = gift.getImage();
        dto.isUsed = gift.getUsedStatus();
        return dto;
    }
}
