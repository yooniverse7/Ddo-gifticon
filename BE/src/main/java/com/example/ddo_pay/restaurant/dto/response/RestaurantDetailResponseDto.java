package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;
import java.util.List;

/**
 * 맛집 상세 조회 시 반환되는 응답 DTO
 * 예: 메뉴 목록, 커스텀 메뉴 목록, 위치 정보 등
 */
@Data
public class RestaurantDetailResponseDto {

	private Long restaurantId;         // PK
	private String placeName;          // 가게명 (기존 resName)
	private String addressName;        // 가게 주소 (기존 resAddress)
	private String mainImageUrl;       // 가게 대표 이미지 (기존 resImage)
	private double starRating;         // 별점
	private String userIntro;          // 나만의 소개(유저가 작성)

	private ResponsePositionDto position; // 위도/경도 정보

	private List<MenuResponseDto> menu;

	private List<CustomMenuResponseDto> customMenu;

	private int visitedCount;

	// 필요에 따라 다른 필드도 추가 가능
}
