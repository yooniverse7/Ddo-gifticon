package com.example.ddo_pay.restaurant.service.crawling;

import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingStoreDto;
import java.util.List;

public interface NaverCrawlingService {

    List<RestaurantCrawlingStoreDto> getCrawlingInfo(RestaurantCrawlingRequestDto requestDto);

    // 필요한 메서드가 더 있다면 선언
}
