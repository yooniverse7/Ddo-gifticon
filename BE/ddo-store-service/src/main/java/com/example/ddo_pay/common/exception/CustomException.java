package com.example.ddo_pay.common.exception;

import java.time.LocalDateTime;

import com.example.ddo_pay.common.response.ErrorResponse;
import com.example.ddo_pay.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class CustomException extends NestedRuntimeException {

    private ResponseCode responseCode;
    private Content content;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public CustomException(ResponseCode responseCode, String field, String message) {
        this(responseCode);
        content = new Content(field, message);
    }

    public ErrorResponse toErrorResponse() {
        return ErrorResponse.builder()
                .errorCode(String.valueOf(responseCode.getCode()))
                .message(content != null ? content.getMessage() : responseCode.getMessage())
                .field(content != null ? content.getField() : null)
                .timestamp(LocalDateTime.now())
                .build();
    }



    @Getter
    @AllArgsConstructor
    private static class Content {
        private String field;
        private String message;
    }
}