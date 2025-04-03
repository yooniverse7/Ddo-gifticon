package com.example.ssafy_bank.common.exception;

import com.example.ssafy_bank.bank.dto.finance_response.CreateAccountResponseDto;
import lombok.Getter;

@Getter
public class SuccessfulAccountCreationException extends RuntimeException {
    private final CreateAccountResponseDto dto;

    public SuccessfulAccountCreationException(CreateAccountResponseDto dto) {
        super("Successful account creation");
        this.dto = dto;
    }

}
