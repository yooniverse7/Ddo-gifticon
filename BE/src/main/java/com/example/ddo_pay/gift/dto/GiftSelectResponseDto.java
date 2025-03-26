package com.example.ddo_pay.gift.dto;

import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.USED;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GiftSelectResponseDto {
    private Long giftId;
    private String sendUserName;
    private LocalDateTime period;
    private String image;
    private USED usedStatus;

    public static GiftSelectResponseDto from(Gift gift) {
        GiftSelectResponseDto dto = new GiftSelectResponseDto();
        dto.giftId = gift.getId();
        dto.sendUserName = gift.getUser().getName();
        dto.period = gift.getPeriod();
        dto.image = gift.getImage();
        dto.usedStatus = gift.getUsedStatus();
        return dto;
    }
}

