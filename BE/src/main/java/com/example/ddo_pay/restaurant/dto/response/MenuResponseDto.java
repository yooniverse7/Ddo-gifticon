package com.example.ddo_pay.restaurant.dto.response;

import lombok.Data;

/**
 * 메뉴 한 건에 대한 정보를 담는 Response DTO
 */
@Data
public class MenuResponseDto {
	private Long menuId;
	private String menuName;
	private String menuPrice;
	private String menuImage;
	// 필요 시 다른 필드도 추가 가능
}
