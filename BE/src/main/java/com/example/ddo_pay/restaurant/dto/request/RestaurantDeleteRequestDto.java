package com.example.ddo_pay.restaurant.dto.request;

import lombok.Data;

/**
 * 맛집 해제(삭제) 요청 시 클라이언트에서 넘겨주는 JSON
 */
@Data
public class RestaurantDeleteRequestDto {
	private Long userId;
	private Long restaurantId;
}
