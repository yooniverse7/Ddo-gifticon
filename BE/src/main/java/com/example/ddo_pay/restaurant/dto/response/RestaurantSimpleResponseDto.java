package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;

/**
 * 맛집 간단 조회 시, 간단 정보를 담는 DTO
 */
@Data
public class RestaurantSimpleResponseDto {
	private Long id;
	private String placeName;
	private String addressName;
	private ResponsePositionDto position;
}
