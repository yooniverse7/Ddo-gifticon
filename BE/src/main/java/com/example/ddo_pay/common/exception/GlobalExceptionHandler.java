package com.example.ddo_pay.common.exception;

import com.example.ddo_pay.common.response.ErrorResponse;
import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.common.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        // 1) 로그 찍기
        ResponseCode code = e.getResponseCode();
        log.error("[CustomException] code={}, message={}", code.getCode(), e.getMessage(), e);

        // 2) httpStatus 설정
        var status = code.getHttpStatus();

        // 3) CustomException이 제공하는 ErrorResponse를 생성
        ErrorResponse errorResponse = e.toErrorResponse();

        // 필요하다면 로그 추가 (field, message 등 확인)
        log.error("[CustomException] code={}, field={}, message={}",
                code.getCode(),
                errorResponse.getField(),
                errorResponse.getMessage(),
                e
        );

        // 4) 최종 응답(Response<ErrorResponse>) 생성 후 반환
        return ResponseEntity
                .status(status)
                .body(Response.create(code, errorResponse));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        log.error("❗ IOException 발생", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "code", "FILE_UPLOAD_FAIL",
                        "message", "파일 업로드 중 문제가 발생했습니다."
                ));
    }
    /**
     * 기타 예외들 처리
     */
    // @ExceptionHandler(NullPointerException.class)
    // public ResponseEntity<?> handleNPE(NullPointerException e) { ... }
}