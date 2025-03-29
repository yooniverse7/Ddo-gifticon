package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;

/**
 * 메뉴 한 건에 대한 정보를 담는 Response DTO
 */
@Data
public class CustomMenuResponseDto {
	private String customMenuName;
	private String customMenuPrice;
	private String customMenuImage;
	// 필요 시 다른 필드도 추가 가능
}
