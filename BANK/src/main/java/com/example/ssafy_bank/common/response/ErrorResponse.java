package com.example.ssafy_bank.common.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String message;
    private String field;
    private LocalDateTime timestamp;
}
