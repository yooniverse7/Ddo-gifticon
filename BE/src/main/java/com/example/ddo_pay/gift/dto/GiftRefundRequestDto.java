package com.example.ddo_pay.gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GiftRefundRequestDto {
    @JsonProperty("giftId")
    private Long giftId;
}

