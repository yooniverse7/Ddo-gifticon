package com.example.ddo_pay.pay.controller;

import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.pay.dto.request.BalanceChargeRequest;
import com.example.ddo_pay.pay.dto.request.RegisterAccountRequest;
import com.example.ddo_pay.pay.dto.request.RegisterPasswordRequest;
import com.example.ddo_pay.pay.dto.response.BalanceResponse;
import com.example.ddo_pay.pay.dto.response.GetAccountResponse;
import com.example.ddo_pay.pay.dto.response.GetPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pay")
public class PayController {

    // 잔액 조회
    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalanceResult(@PathVariable("userId") int userId) {
        BalanceResponse balanceResponse;

        if(userId == 1) {
            balanceResponse = BalanceResponse.builder()
                    .payBalance(20000)
                    .payPoint(50)
                    .build();
        } else {
            balanceResponse = BalanceResponse.builder()
                    .payBalance(0)
                    .payPoint(0)
                    .build();
        }

        Response<?> response = Response.create(ResponseCode.SUCCESS_BALANCE_CHECK, balanceResponse);
        return ResponseEntity.status(200).body(response);
    }

    // 등록된 계좌 확인
    @GetMapping("/account/{userId}")
    public ResponseEntity<?> getRegisteredAccount(@PathVariable("userId") int useId) {
        GetAccountResponse getAccountResponse;

        if(useId == 1) {
            getAccountResponse = GetAccountResponse.builder()
                    .accountId(1)
                    .accountBank("싸피은행")
                    .accountNumber("3333-33-333-3333")
                    .build();
        } else {
            getAccountResponse = GetAccountResponse.builder()
                    .accountId(0)
                    .accountBank("")
                    .accountNumber("")
                    .build();
        }

        Response<?> response = Response.create(ResponseCode.SUCCESS_ACCOUNT_CHECK, getAccountResponse);
        return ResponseEntity.status(200).body(response);

    }


    // 포인트 조회
    @GetMapping("/point/{userId}")
    public ResponseEntity<?> getPointResult(@PathVariable("userId") int useId) {
        GetPointResponse getPointResponse;

        if(useId == 1) {
            getPointResponse = GetPointResponse.builder()
                    .payPoint(5)
                    .build();
        } else {
            getPointResponse = GetPointResponse.builder()
                    .payPoint(5)
                    .build();
        }

        Response<?> response = Response.create(ResponseCode.SUCCESS_POINT_CHECK, getPointResponse);
        return ResponseEntity.status(200).body(response);

    }

    // 등록된 계좌 삭제
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteRegisteredAccount(@PathVariable("accountId") int accountId) {
        Response<?> response = Response.create(ResponseCode.SUCCESS_DELETE_ACCOUNT, null);
        return ResponseEntity.status(200).body(response);

    }

    // 새로운 계좌 등록
    @PostMapping({"/account"})
    public ResponseEntity<?> registerNewAccount(@RequestBody RegisterAccountRequest request) {
        Response<?> response = Response.create(ResponseCode.SUCCESS_REGISTER_ACCOUNT, null);
        return ResponseEntity.status(200).body(response);

    }

    // 기프티콘 환불 요청
    @PostMapping({"/refund"})
    public ResponseEntity<?> refundGift(@RequestBody RegisterAccountRequest request) {
        Response<?> response = Response.create(ResponseCode.SUCCESS_REFUND_GIFT, null);
        return ResponseEntity.status(200).body(response);

    }

    // 결제 비밀번호 등록
    @PostMapping({"/password"})
    public ResponseEntity<?> registerPassword(@RequestBody RegisterPasswordRequest request) {
        Response<?> response = Response.create(ResponseCode.SUCCESS_REGISTER_PASSWORD, null);
        return ResponseEntity.status(200).body(response);

    }

    // 잔액 충전 (PATCH /api/pay/balance)
    @PatchMapping("/balance")
    public ResponseEntity<?> chargeBalance(@RequestBody BalanceChargeRequest request) {
        // 필요한 비즈니스 로직을 추가할 수 있습니다.
        Response<?> response = Response.create(ResponseCode.SUCCESS_BALANCE_CHARGE, null);
        return ResponseEntity.status(200).body(response);
    }

}
