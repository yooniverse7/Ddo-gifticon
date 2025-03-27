package com.example.ddo_pay.restaurant.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingStoreDto;
import com.example.ddo_pay.restaurant.service.NaverCrawlingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantCrawlingController {

	private final NaverCrawlingService crawlingService;

	@GetMapping("/crawling")
	public ResponseEntity<?> getCrawlingInfo(@RequestParam String data) {
		// 1) Base64 디코딩
		String decodedJson = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);

		// 2) 디코딩된 JSON을 파싱해서 DTO로 매핑
		// 예: JSON 내용이
		//   {
		//       "place_name": "하단끝집 하단점",
		//       "address_name": "부산 사하구 낙동대로535번길 23 1층"
		//   }
		// 라고 하면, Jackson 등으로 아래와 같은 DTO에 매핑할 수 있음:
		//   class CrawlingRequestDto { String placeName; String addressName; ... }

		RestaurantCrawlingRequestDto requestDto = parseJsonToDto(decodedJson);

		// 3) 서비스 호출 → 크롤링 로직 수행 후 결과 받기
		List<RestaurantCrawlingStoreDto> result = crawlingService.getCrawlingInfo(requestDto);

		return ResponseEntity.ok(result);
	}

	// Jackson ObjectMapper 예시
	private RestaurantCrawlingRequestDto parseJsonToDto(String json) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// 파싱 대상 클래스도 RestaurantCrawlingRequestDto.class 로
			return objectMapper.readValue(json, RestaurantCrawlingRequestDto.class);
		} catch (Exception e) {
			throw new RuntimeException("JSON 파싱 실패", e);
		}


	}
}