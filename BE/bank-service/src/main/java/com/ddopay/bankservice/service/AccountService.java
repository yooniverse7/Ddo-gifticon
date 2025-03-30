package com.ddopay.bankservice.service;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public boolean verify(String accountNumber, String userId) {
        // 예시: accountNumber가 111-222-333 이면 인증된 걸로 처리
        return accountNumber != null && accountNumber.startsWith("111");
    }
}

