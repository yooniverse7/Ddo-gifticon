package com.example.ddo_pay.pay.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAccountRequest {
    private Long userId;
    @JsonProperty("randomWord")
    private String randomWord;
}
