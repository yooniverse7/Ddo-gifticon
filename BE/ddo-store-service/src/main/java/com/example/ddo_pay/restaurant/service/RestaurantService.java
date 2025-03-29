package com.example.ddo_pay.restaurant.service;

import com.example.ddo_pay.restaurant.dto.request.*;
import com.example.ddo_pay.restaurant.dto.response.*;
import com.example.ddo_pay.restaurant.dto.response.RestaurantDetailResponseDto;

import java.util.List;

public interface RestaurantService {

	/**
	 * (1) 맛집 등록
	 *  POST /api/restaurants
	 */
	void createRestaurant(RestaurantCreateRequestDto requestDto);

	/**
	 * (2) 맛집 제거
	 *  DELETE /api/restaurants
	 */
	void removeRestaurant(RestaurantDeleteRequestDto requestDto);

	/**
	 * (3) 등록 맛집 리스트 조회
	 *  GET /api/restaurants
	 */
	List<RestaurantListItemResponseDto> getRegisteredRestaurantList();

	/**
	 * (4) 맛집 상세 조회
	 *  GET /api/restaurants/{restaurantId}
	 *  - 메뉴 + 커스텀 메뉴 + 나머지 상세 정보
	 */
	RestaurantDetailResponseDto getRestaurantDetail(Long restaurantId);

	/**
	 * (5) 커스텀 메뉴 등록
	 *  POST /api/restaurants/custom
	 */
	void createCustomMenu(CustomMenuRequestDto requestDto);

	/**
	 * (6) 커스텀 메뉴 삭제
	 *  DELETE /api/restaurants/custom/{customId}
	 */
	void deleteCustomMenu(Long customId);

	// 크롤링된 StoreDto를 DB에 저장
	void saveCrawlingStoreData(RestaurantCrawlingStoreDto storeDto, Long userId);

}
