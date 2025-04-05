package com.example.ddo_pay.gift.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GiftCreateRequestDto {

    // 기프티콘 커스텀 DATA
    private String title;
    private int amount;
    private String message;

    @JsonProperty("menu_name")
    private String menuName;

    // 대상자의 phoneNum
    @JsonProperty("phone_num")
    private String phoneNum;

    // 담을 맛집의 메뉴들에 대한 정보
    @JsonProperty("res_id")
    private int resId;

    private Position position;

    @Data
    private static class Position {
        private double lat;
        private double lng;
    }
}
