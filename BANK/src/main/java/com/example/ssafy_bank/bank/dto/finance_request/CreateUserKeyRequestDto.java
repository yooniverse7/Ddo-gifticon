package com.example.ssafy_bank.bank.dto.finance_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 계정 + userkey 생성

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserKeyRequestDto {
    @JsonProperty("apiKey")
    private String apiKey;
    @JsonProperty("userId")
    private String userId;
}
