package com.example.ddo_pay.restaurant.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 맛집 등록 시, 클라이언트가 전달하는 Request DTO
 */
@Data
public class RestaurantCreateRequestDto {

	private Long userId;

	private String placeName;       // 가게명 (기존 resName)

	private String addressName;     // 가게 주소 (기존 resAddress)

	private String mainImageUrl;    // 가게 이미지 (기존 resImage)

	private RequestPositionDto position; // 위도, 경도 정보

	private String userIntro;       // 나만의 소개 (기존 userIntro)

	private BigDecimal starRating;  // 별점 (기존 starRating)

	// 메뉴 목록
	private List<RestaurantMenuRequestDto> menu;
	private List<CustomMenuRequestDto> customMenu;

	// 기본값 0
	private int visitedCount;
}
