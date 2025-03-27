package com.example.ddo_pay.restaurant.dto.request;

import lombok.Data;

/**
 * 커스텀 메뉴 등록 시, 클라이언트에서 전달하는 JSON
 */
@Data
public class CustomMenuRequestDto {
	private Long userId;
	private Long restaurantId;
	private String customMenuName;
	private int customMenuPrice;
	private String customMenuImage;
}
