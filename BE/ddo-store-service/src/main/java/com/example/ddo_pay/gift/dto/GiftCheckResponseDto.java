package com.example.ddo_pay.gift.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GiftCheckResponseDto {
    private Boolean available;
}
