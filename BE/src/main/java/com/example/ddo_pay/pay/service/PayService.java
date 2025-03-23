package com.example.ddo_pay.pay.service;

import com.example.ddo_pay.pay.dto.request.BalanceChargeRequest;
import com.example.ddo_pay.pay.dto.request.RegisterAccountRequest;
import com.example.ddo_pay.pay.dto.request.RegisterPasswordRequest;
import com.example.ddo_pay.pay.dto.response.BalanceResponse;
import com.example.ddo_pay.pay.dto.response.GetAccountResponse;
import com.example.ddo_pay.pay.dto.response.GetPointResponse;
import org.springframework.stereotype.Service;

@Service
public interface PayService {

    // 잔액 조회
    public BalanceResponse selectBalance(int userId) ;

    // 등록된 계좌 확인
    public GetAccountResponse selectAccount(int userId) ;

    // 포인트 조회
    public GetPointResponse selectPoint(int userId) ;

    // 등록된 계좌 삭제
    public void deleteAccount(int accountId) ;

    // 새로운 계좌 등록
    public void creatAccount(RegisterAccountRequest dto) ;

    // 기프티콘 환불
    /*
    * 해당 기프티콘 status 변경, 금액 받아와야 함
    * 받아온 금액만큼 잔고 변경되어야 함
    * 결제 내역에 입금 history 생성되어야 함
    * */
    public void refund(int giftId) ;

    // 결제 비밀번호 등록
    public void createPassword(RegisterPasswordRequest dto);

    // 잔액 충전
    public void updateBalance(BalanceChargeRequest dto);

}
