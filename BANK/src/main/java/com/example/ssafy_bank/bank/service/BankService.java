package com.example.ssafy_bank.bank.service;

import java.util.List;
import java.util.Map;

import com.example.ssafy_bank.bank.dto.ddopay_request.ChargeDdoPayRequestDto;
import com.example.ssafy_bank.bank.dto.ddopay_response.BankChargeResponseDto;
import com.example.ssafy_bank.bank.dto.request.TransactionSummaryDto;
import com.example.ssafy_bank.bank.dto.response.BalanceResponseDto;
import com.example.ssafy_bank.bank.dto.response.LoginResponseDto;

public interface BankService {

    // 계정 + userKey + 계좌 생성
    void createUserKey(String email);

    // 이메일 로그인
    LoginResponseDto login(String email);

    // 계좌 내역 조회
    List<TransactionSummaryDto> selectHistory(Long userId);

    // 잔액 조회
    BalanceResponseDto getBalance(Long userId);


    // 또페이 계좌이체 요청
    BankChargeResponseDto chargeDdoPay(ChargeDdoPayRequestDto request);
}
