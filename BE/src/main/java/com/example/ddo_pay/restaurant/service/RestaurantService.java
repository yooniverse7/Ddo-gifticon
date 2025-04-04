package com.example.ddo_pay.restaurant.service;

import com.example.ddo_pay.common.util.SecurityUtil;
import com.example.ddo_pay.restaurant.dto.request.*;
import com.example.ddo_pay.restaurant.dto.response.*;
import com.example.ddo_pay.restaurant.dto.response.RestaurantDetailResponseDto;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import com.example.ddo_pay.restaurant.entity.UserRestaurant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {

	/**
	 * (1) 맛집 등록
	 *  POST /api/restaurants
	 */
	void createRestaurant(RestaurantCreateRequestDto requestDto);
	// 새로 추가: 파일도 함께 처리하는 식당 등록 메서드
	void createRestaurant(RestaurantCreateRequestDto requestDto, MultipartFile mainImageFile, List<MultipartFile> customMenuImages);

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
	 * ─────────────────────────────────────────────
	 *  새로 추가: 위치 기반 맛집 조회
	 * ─────────────────────────────────────────────
	 */
	List<RestaurantListItemResponseDto> getRegisteredRestaurantListByPosition(Double lat, Double lng);

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
