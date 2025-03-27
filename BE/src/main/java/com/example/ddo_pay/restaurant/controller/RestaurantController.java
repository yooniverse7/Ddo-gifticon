package com.example.ddo_pay.restaurant.controller;

import static com.example.ddo_pay.common.response.ResponseCode.*;

import java.util.List;

import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.restaurant.dto.request.CustomMenuRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCreateRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantDeleteRequestDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantDetailResponseDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantListItemResponseDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantSimpleResponseDto;
import com.example.ddo_pay.restaurant.dto.response.ResponsePositionDto;
import com.example.ddo_pay.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 실제 DB 연동: Service 호출을 통해 비즈니스 로직 수행
 */
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

	private final RestaurantService restaurantService; // Service 주입

	/**
	 * 맛집 등록 (POST /api/restaurants)
	 */
	@PostMapping
	public ResponseEntity<?> create(@RequestBody RestaurantCreateRequestDto requestDto) {
		// 실제 DB 연동
		restaurantService.createRestaurant(requestDto);

		return ResponseEntity
				.status(SUCCESS_CREATE_RESTAURANT.getHttpStatus())  // 204, 혹은 201 등
				.body(Response.create(SUCCESS_CREATE_RESTAURANT, null));
	}

	/**
	 * 맛집 해제(삭제) (DELETE /api/restaurants)
	 */
	@DeleteMapping
	public ResponseEntity<?> removeRestaurant(@RequestBody RestaurantDeleteRequestDto requestDto) {
		// 실제 DB 연동
		restaurantService.removeRestaurant(requestDto);

		return ResponseEntity
				.status(SUCCESS_REMOVE_RESTAURANT.getHttpStatus())  // 200
				.body(Response.create(SUCCESS_REMOVE_RESTAURANT, null));
	}

	/**
	 * 등록된 맛집 리스트 조회 (GET /api/restaurants)
	 */
	@GetMapping
	public ResponseEntity<?> getRegisteredRestaurantList(
			@RequestParam(required = false) Double lat,
			@RequestParam(required = false) Double lng
	) {
		// lat, lng가 넘어오는 경우 사용
		// 넘어오지 않을 경우엔 null 이므로, 추가 처리나 기본값 설정 가능

		// 실제 DB 연동
		List<RestaurantListItemResponseDto> list = restaurantService.getRegisteredRestaurantList();

		return ResponseEntity
				.status(SUCCESS_GET_RESTAURANT_LIST.getHttpStatus())
				.body(Response.create(SUCCESS_GET_RESTAURANT_LIST, list));
	}


	/**
	 * 맛집 상세 조회 (GET /api/restaurants/{restaurantId})
	 * - 간단 정보 혹은 상세 정보 모두 가능
	 */
	@GetMapping("/{restaurantId}")
	public ResponseEntity<?> getSimpleRestaurantInfo(@PathVariable Long restaurantId) {

		// 실제 DB 엔티티 조회
		// Service 로직에서 RestaurantDetailResponseDto 등을 리턴한다고 가정
		RestaurantDetailResponseDto detail = restaurantService.getRestaurantDetail(restaurantId);

		return ResponseEntity
				.ok(
						Response.create(ResponseCode.SUCCESS_GET_SIMPLE_RESTAURANT, detail)
				);
	}

	/**
	 * 커스텀 메뉴 등록 (POST /api/restaurants/custom)
	 */
	@PostMapping("/custom")
	public ResponseEntity<?> createCustomMenu(@RequestBody CustomMenuRequestDto requestDto) {
		// 실제 등록 로직
		restaurantService.createCustomMenu(requestDto);

		return ResponseEntity
				.status(SUCCESS_CREATE_CUSTOM_MENU.getHttpStatus())  // 200
				.body(Response.create(SUCCESS_CREATE_CUSTOM_MENU, null));
	}

	/**
	 * 커스텀 메뉴 삭제 (DELETE /api/restaurants/custom/{customId})
	 */
	@DeleteMapping("/custom/{customId}")
	public ResponseEntity<?> deleteCustomMenu(@PathVariable Long customId) {
		// 실제 비즈니스 로직
		restaurantService.deleteCustomMenu(customId);

		return ResponseEntity
				.status(SUCCESS_DELETE_CUSTOM_MENU.getHttpStatus())  // 200
				.body(Response.create(SUCCESS_DELETE_CUSTOM_MENU, null));
	}
}
