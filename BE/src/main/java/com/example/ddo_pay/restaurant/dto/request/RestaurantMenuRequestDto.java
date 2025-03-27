package com.example.ddo_pay.restaurant.dto.request;

import lombok.Data;

/**
 * 맛집 등록 시, 각 메뉴 정보를 담는 DTO
 */
@Data
public class RestaurantMenuRequestDto {
	private Long userId;
	private String menuName;
	private int menuPrice;
	private String menuImage;  // URL
}
