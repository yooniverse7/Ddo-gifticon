package com.example.ddo_pay.pay.service;

import com.example.ddo_pay.pay.dto.request.AccountVerifyRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PayService {

    // 계좌 유효 확인 로직
    void verifyAccount(AccountVerifyRequest request, Long userId);


}
