package com.example.ssafy_bank.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
    private String code;
    private String message;
    private T data;



    public static <T> Response<T> create(ResponseCode responseCode, T data) {
        return new Response<>(
                String.valueOf(responseCode.getCode()),
                responseCode.getMessage(),
                data
        );
    }
}