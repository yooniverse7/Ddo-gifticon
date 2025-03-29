package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;
import java.util.List;

/**
 * 최종 응답: 여러 개의 음식점 정보를 담는 구조
 */
@Data
public class RestaurantCrawlingResponseDto {
	private List<RestaurantCrawlingStoreDto> stores;
}
