package com.example.ddo_pay.pay.service;


import com.example.ddo_pay.pay.dto.request.AccountVerifyRequest;
import com.example.ddo_pay.pay.dto.request.RegisterAccountRequest;
import com.example.ddo_pay.pay.dto.request.RegisterPasswordRequest;

import com.example.ddo_pay.pay.dto.response.GetAccountResponse;
import com.example.ddo_pay.pay.dto.response.GetBalanceResponse;
import com.example.ddo_pay.pay.dto.response.GetPointResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PayService {


    // 계좌 유효 확인 로직
    String verifyAccount(Long userId, AccountVerifyRequest request);

    // 본인 계좌 인증 후 계좌 등록
    void registerAccount(Long userId, RegisterAccountRequest request);

    // 비밀번호 등록 및 또페이 생성
    void registerPayPassword(Long userId, RegisterPasswordRequest request);

    // 잔고 확인
    GetBalanceResponse selectBalance(Long userId);

    // 포인트 조회
    GetPointResponse selectPoint(Long userId);

    // 연결된 계좌 조회
    List<GetAccountResponse> selectAccountList(Long userId);
}
