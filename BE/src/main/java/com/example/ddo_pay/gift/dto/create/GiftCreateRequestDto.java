package com.example.ddo_pay.gift.dto.create;

import lombok.Data;

import java.util.List;

@Data
public class GiftCreateRequestDto {

    // 기프티콘 커스텀 DATA
    private String giftTitle;
    private int amount;
    private String message;
    private String image;

    // 대상자의 phoneNum
    private String phoneNum;

    // 생성하는 사용자 ID
    private Long userId;

    // 담을 맛집의 메뉴들에 대한 정보
    private RestaurantDto restaurant;

    @Data
    public static class RestaurantDto {
        private Long id;
        private List<MenuInfoDto> menuDtoList;
    }

    @Data
    public static class MenuInfoDto {
        private Long id;
        private String menuName;
        private int menuAmount;
        private int menuCount;
    }
}
