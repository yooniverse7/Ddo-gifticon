package com.example.ddo_pay.gift.dto;


import lombok.Data;

import com.example.ddo_pay.gift.entity.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class SendGiftSelectResponseDto {

    private Long id;
    private String title;
    private String message;
    private String image;
    private int amount;
    private LocalDateTime expirationDate;
    private String menuName;
    private String phoneNum;
    private USED usedStatus;
    private int resId;
    private Position position;

    @Data
    @NoArgsConstructor
    private static class Position {
        private double lat;
        private double lng;

        public Position(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    public static SendGiftSelectResponseDto from(Gift gift) {
        SendGiftSelectResponseDto dto = new SendGiftSelectResponseDto();
        dto.id = gift.getId();
        dto.title = gift.getTitle();
        dto.message = gift.getMessage();
        dto.image = gift.getImage();
        dto.amount = gift.getAmount();
        dto.expirationDate = gift.getExpirationDate();
        dto.menuName = gift.getMenuCombination();
        dto.phoneNum = gift.getPhoneNum();
        dto.usedStatus = gift.getUsedStatus();
        dto.resId = Math.toIntExact(gift.getRestaurant().getId());
        dto.position = new Position(gift.getRestaurant().getLat(), gift.getRestaurant().getLng());
        return dto;
    }

}
