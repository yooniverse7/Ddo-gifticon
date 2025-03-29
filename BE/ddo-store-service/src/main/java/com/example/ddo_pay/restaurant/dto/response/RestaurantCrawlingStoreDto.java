package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 특정 가게(음식점)의 정보 + 메뉴 목록
 */
@Data
public class RestaurantCrawlingStoreDto {
	private Long id;
	private String placeName;     // 1. 매장 이름
	private String mainImageUrl;  // 2. 이미지 URL
	private String addressName;       // 3. 매장 주소
	private ResponsePositionDto position; // 4. 매장 경도, 위도
	private String storeInfo;     // 5. 매장 소개
	private BigDecimal starRating;
	private String userIntro;
	private String placeId;
	// 메뉴 탭 목록
	private List<RestaurantCrawlingMenuDto> menus;
}
