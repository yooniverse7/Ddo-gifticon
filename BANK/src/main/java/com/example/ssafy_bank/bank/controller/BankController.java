package com.example.ssafy_bank.bank.controller;

import com.example.ssafy_bank.bank.dto.request.EmailRequestDto;
import com.example.ssafy_bank.bank.service.BankService;
import com.example.ssafy_bank.common.response.Response;
import com.example.ssafy_bank.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankController {

    private final BankService bankService;

    // 계정 생성 및 계좌 생성
    @PostMapping("/create-account")
    public Response<Object> createUserKey(@RequestBody EmailRequestDto request) {
        bankService.createUserKey(request.getEmail());
        return Response.create(ResponseCode.SUCCESS_CREATE_USER_KEY, null);
    }

}
