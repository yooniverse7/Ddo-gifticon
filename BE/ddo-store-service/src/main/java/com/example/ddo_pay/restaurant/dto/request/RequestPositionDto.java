package com.example.ddo_pay.restaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPositionDto {
	private double lat;
	private double lng;
}