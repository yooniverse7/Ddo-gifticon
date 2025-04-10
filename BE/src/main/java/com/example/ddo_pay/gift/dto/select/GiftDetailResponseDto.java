package com.example.ddo_pay.gift.dto.select;


import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.USED;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class GiftDetailResponseDto {

    private Long id;
    private String sendUserName;
    private String title;
    private int amount;
    private String phoneNum;
    private String message;
    private String image;
    private LocalDateTime expirationDate;
    private USED usedStatus;
    private Long restaurantId;
    private String restaurantName;
    private Position position;

    public static GiftDetailResponseDto from(Gift gift) {
        GiftDetailResponseDto dto = new GiftDetailResponseDto();
        dto.id = gift.getId();
        dto.sendUserName= gift.getUser().getName();
        dto.title = gift.getTitle();
        dto.amount = gift.getAmount();
        dto.phoneNum = gift.getPhoneNum();
        dto.message = gift.getMessage();
        dto.image = gift.getImage();
        dto.expirationDate = gift.getExpirationDate();
        dto.usedStatus = gift.getUsedStatus();
        dto.restaurantId = gift.getRestaurant().getId();
        dto.restaurantName = gift.getRestaurant().getPlaceName();
        Restaurant restaurant = gift.getRestaurant();
        dto.position = new Position(restaurant.getLat(), restaurant.getLng());
        return dto;
    }

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
}
