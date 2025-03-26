package com.example.ddo_pay.restaurant.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ddo_pay.restaurant.dto.request.CustomMenuRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCrawlingRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantCreateRequestDto;
import com.example.ddo_pay.restaurant.dto.request.RestaurantDeleteRequestDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantCrawlingResponseDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantDetailResponseDto;
import com.example.ddo_pay.restaurant.dto.response.RestaurantListItemResponseDto;
import com.example.ddo_pay.restaurant.entity.UserRestaurant;
import com.example.ddo_pay.restaurant.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Override
	public void createRestaurant(RestaurantCreateRequestDto requestDto) {
		UserRestaurant userRestaurant = new UserRestaurant();
	}

	@Override
	public void removeRestaurant(RestaurantDeleteRequestDto requestDto) {

	}

	@Override
	public List<RestaurantListItemResponseDto> getRegisteredRestaurantList() {
		return null;
	}

	@Override
	public RestaurantDetailResponseDto getRestaurantDetail(Long restaurantId) {
		return null;
	}

	@Override
	public void createCustomMenu(CustomMenuRequestDto requestDto) {

	}

	@Override
	public void deleteCustomMenu(Long customId) {

	}

	@Override
	public RestaurantCrawlingResponseDto getCrawlingInfo(List<RestaurantCrawlingRequestDto> requestList) {
		return null;
	}
}