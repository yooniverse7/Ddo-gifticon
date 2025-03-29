package com.example.ddo_pay.restaurant.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.example.ddo_pay.restaurant.dto.response.ResponsePositionDto;

import java.math.BigDecimal;

/**
 * GET 방식으로 위도, 경도를 전달받기 위한 DTO.
 * 주의: @GetMapping + @ModelAttribute 로 바인딩 (또는 Controller에서 @RequestParam)
 */
@Data
public class RestaurantCrawlingRequestDto {
	private ResponsePositionDto position;
	private Long userId;

	@JsonProperty("place_name")
	private String placeName;

	@JsonProperty("address_name")
	private String addressName;
	private BigDecimal starRating;
}
