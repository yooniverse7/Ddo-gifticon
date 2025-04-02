package com.example.ssafy_bank.common.exception;

import com.example.ssafy_bank.common.response.ErrorResponse;
import com.example.ssafy_bank.common.response.Response;
import com.example.ssafy_bank.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<ErrorResponse>> handleCustomException(CustomException e) {
        ResponseCode code = e.getResponseCode();
        ErrorResponse errorResponse = e.toErrorResponse();

        log.error("[CustomException] code={}, field={}, message={}",
                code.getCode(),
                errorResponse.getField(),
                errorResponse.getMessage()
        );

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(Response.create(code, errorResponse));
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<ErrorResponse>> handleException(Exception e) {
        ResponseCode code = ResponseCode.INTERNAL_SERVER_ERROR;

        log.error("[Exception] message={}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(String.valueOf(code.getCode()))
                .message(code.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(Response.create(code, errorResponse));
    }
}
