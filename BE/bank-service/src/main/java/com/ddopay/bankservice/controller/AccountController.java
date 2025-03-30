package com.ddopay.bankservice.controller;
import com.ddopay.bankservice.dto.BankVerifyRequest;
import com.ddopay.bankservice.dto.BankVerifyResponse;
import com.ddopay.bankservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private static final Logger log = Logger.getLogger(AccountService.class.getName());
    @PostMapping("/verify")
    public ResponseEntity<BankVerifyResponse> verifyAccount(@RequestBody BankVerifyRequest request) {

        log.info("bank-service-request 발생");
        boolean result = accountService.verify(request.getAccountNumber(), request.getUserId());

        BankVerifyResponse response = new BankVerifyResponse(result);
        return ResponseEntity.ok(response);
    }
}

