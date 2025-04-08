package com.example.ddo_pay.gift.dto;

import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.USED;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GiftSelectResponseDto {
    private Long id;
    private String title;
    private int amount;
    private String sendUserName;
    private LocalDateTime expirationDate;
    private String image;
    private USED usedStatus;
    private Position position;

    public static GiftSelectResponseDto from(Gift gift) {
        GiftSelectResponseDto dto = new GiftSelectResponseDto();
        dto.id = gift.getId();
        dto.title = gift.getTitle();
        dto.amount = gift.getAmount();
        dto.sendUserName = gift.getUser().getName();
        dto.expirationDate = gift.getExpirationDate();
        dto.image = gift.getImage();
        dto.usedStatus = gift.getUsedStatus();
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

