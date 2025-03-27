package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;

/**
 * 등록 맛집 리스트 조회 시 반환되는 맛집 정보 DTO
 */
@Data
public class RestaurantListItemResponseDto {
	private Long id;
	private String placeName;         // 가게명
	private String mainImageUrl;      // 가게 대표 이미지 (기존 restaurantImage)
	private String addressName;       // 가게 주소
	private ResponsePositionDto position;
	private int visitedCount;
}
