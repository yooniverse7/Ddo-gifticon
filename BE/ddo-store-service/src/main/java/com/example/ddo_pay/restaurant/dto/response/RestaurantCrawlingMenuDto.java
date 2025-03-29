package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;

/**
 * 메뉴 한 건에 대한 정보
 */
@Data
public class RestaurantCrawlingMenuDto {
	private String menuName;   // 메뉴 이름
	private String menuDesc;   // 메뉴 소개
	private String menuPrice;  // 메뉴 가격
	private String menuImage;  // 메뉴 이미지 URL
}
