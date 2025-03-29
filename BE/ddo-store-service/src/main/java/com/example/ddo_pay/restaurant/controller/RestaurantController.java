package com.example.ddo_pay.restaurant.controller;

import static com.example.ddo_pay.common.response.ResponseCode.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.restaurant.dto.request.*;
import com.example.ddo_pay.restaurant.dto.response.*;
import com.example.ddo_pay.restaurant.service.RestaurantService;
import com.example.ddo_pay.restaurant.service.crawling.NaverCrawlingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
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
	private final RedisTemplate<String, String> redisTemplate;
	private final NaverCrawlingService naverCrawlingService;


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

	/**
	 * (2) 크롤링 요청
	 *  - GET /api/restaurants/crawling?data={Base64EncodedJSON}
	 *    Base64 안에는 { "place_name": "...", "address_name": "..." } 구조
	 */
	@GetMapping("/crawling")
	public ResponseEntity<?> getCrawlingInfo(@RequestParam String data) {
		// 1) Base64 decode
		String decodedJson = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);

		// 2) JSON → DTO
		RestaurantCrawlingRequestDto requestDto = parseJsonToDto(decodedJson);

		// 3) 크롤링 수행
		List<RestaurantCrawlingStoreDto> crawlingResult = naverCrawlingService.getCrawlingInfo(requestDto);

		// (A) placeId 가져오기 (예: 첫 번째 store)
		String placeId = null;
		if (!crawlingResult.isEmpty()) {
			placeId = crawlingResult.get(0).getPlaceId();
		}
		// 만약 여러 매장이 있을 수 있고, 각각에 placeId가 다르다면,
		// 필요에 맞춰 로직 작성 (여기서는 1개만 처리)

		// 4) List → JSON 문자열 (Jackson)
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonList;
		try {
			jsonList = objectMapper.writeValueAsString(crawlingResult);
		} catch (JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("JSON 직렬화 에러: " + e.getMessage());
		}

		// 5) Redis에 문자열로 저장
		String key;
		if (placeId != null) {
			key = "crawling:" + placeId;  // ★ placeId 기반
		} else {
			// placeId를 못 구한 경우 fallback
			key = "crawling:unknown";
		}

		redisTemplate.opsForValue().set(key, jsonList, Duration.ofHours(4));

		// 6) 응답: cacheKey + 실제 크롤링 결과
		Map<String, Object> response = new HashMap<>();
		response.put("cacheKey", key);
		response.put("stores", crawlingResult);
		return ResponseEntity.ok(response);
	}


	/**
	 * (3) 맛집 등록 (크롤링 데이터를 이용)
	 *  - POST /api/restaurants/register
	 *    요청 바디: { "cacheKey": "...", "storeIndex": 0, "userId": 123 }
	 *    1) Redis에서 cacheKey로 목록 가져오기
	 *    2) storeIndex에 해당하는 매장 데이터 선택
	 *    3) DB에 최종 등록
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerRestaurant(@RequestBody RegisterCrawledStoreRequest registerRequest) {
		String cacheKey = registerRequest.getCacheKey();
		int storeIndex = registerRequest.getStoreIndex();
		Long userId = registerRequest.getUserId();

		// 1) Redis에서 문자열(JSON) 꺼내기
		String jsonStored = redisTemplate.opsForValue().get(cacheKey);
		if (jsonStored == null) {
			throw new RuntimeException("유효하지 않은 캐싱 정보 (Redis에 없음)");
		}

		// 2) JSON → List<RestaurantCrawlingStoreDto>
		ObjectMapper objectMapper = new ObjectMapper();
		List<RestaurantCrawlingStoreDto> cachedList;
		try {
			cachedList = objectMapper.readValue(
					jsonStored, new TypeReference<List<RestaurantCrawlingStoreDto>>() {}
			);
		} catch (Exception e) {
			throw new RuntimeException("JSON 파싱 실패", e);
		}

		// 3) 유효성 검사
		if (storeIndex < 0 || storeIndex >= cachedList.size()) {
			throw new RuntimeException("storeIndex 범위 초과");
		}

		// 4) 선택 매장 + DB 저장
		RestaurantCrawlingStoreDto selectedStore = cachedList.get(storeIndex);
		restaurantService.saveCrawlingStoreData(selectedStore, userId);

		return ResponseEntity.ok("등록 완료");
	}

	/**
	 * private: JSON 파싱 유틸
	 */
	private RestaurantCrawlingRequestDto parseJsonToDto(String json) {
		try {
			return new ObjectMapper().readValue(json, RestaurantCrawlingRequestDto.class);
		} catch (Exception e) {
			throw new RuntimeException("JSON 파싱 실패", e);
		}
	}

}
