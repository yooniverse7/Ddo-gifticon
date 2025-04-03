package com.example .ssafy_bank.bank.controller;

import com.example.ssafy_bank.bank.dto.ddopay_request.ChargeDdoPayRequestDto;
import com.example.ssafy_bank.bank.dto.ddopay_response.BankChargeResponseDto;
import com.example.ssafy_bank.bank.dto.request.EmailRequestDto;
import com.example.ssafy_bank.bank.dto.request.TransactionSummaryDto;
import com.example.ssafy_bank.bank.dto.request.UserIdRequestDto;
import com.example.ssafy_bank.bank.dto.response.BalanceResponseDto;
import com.example.ssafy_bank.bank.dto.response.LoginResponseDto;
import com.example.ssafy_bank.bank.service.BankService;
import com.example.ssafy_bank.common.response.Response;
import com.example.ssafy_bank.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // 이메일로 로그인
    @PostMapping("/login")
    public Response<Object> emailLogin(@RequestBody EmailRequestDto request) {
        LoginResponseDto response = bankService.login(request.getEmail());
        return Response.create(ResponseCode.SUCCESS_LOGIN, response);
    }

    // 계좌 내역 조회
    @PostMapping("/list")
    public Response<Object> selectHistory(@RequestBody UserIdRequestDto request) {
        List<TransactionSummaryDto> summaries = bankService.selectHistory(request.getUserId());
        return Response.create(ResponseCode.SUCCESS_SELECT_HISTORY, summaries);
    }

    // 잔액 조회
    @PostMapping("/balance")
    public Response<Object> selectBalance(@RequestBody UserIdRequestDto request) {
        BalanceResponseDto response = bankService.getBalance(request.getUserId());
        return Response.create(ResponseCode.SUCCESS_SELECT_BALANCE, response);
    }

    // 또페이 충전 API 추가
    @PostMapping("/charge-ddopay")
    public ResponseEntity<BankChargeResponseDto> chargeDdoPay(@RequestBody ChargeDdoPayRequestDto request) {
        BankChargeResponseDto response = bankService.chargeDdoPay(request);
        return ResponseEntity.ok(response);
    }

}
