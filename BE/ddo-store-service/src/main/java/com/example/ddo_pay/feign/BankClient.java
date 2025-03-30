package com.example.ddo_pay.feign;

import com.example.ddo_pay.feign.dto.BankVerifyRequest;
import com.example.ddo_pay.feign.dto.BankVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-service", url = "http://bank-service:8082")
public interface BankClient {

    @PostMapping("/api/account/verify")
    BankVerifyResponse verifyAccount(@RequestBody BankVerifyRequest request);
}
