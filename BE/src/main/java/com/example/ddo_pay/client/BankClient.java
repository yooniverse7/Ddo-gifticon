package com.example.ddo_pay.client;

import com.example.ddo_pay.pay.dto.bank_request.BankDdoPayChargeRequest;
import com.example.ddo_pay.pay.dto.bank_response.BankChargeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bankClient", url = "${bank.server.url:https://j12e106.p.ssafy.io}")
public interface BankClient {
    @PostMapping("/bank/charge-ddopay")
    ResponseEntity<BankChargeResponseDto> chargeDdoPay(@RequestBody BankDdoPayChargeRequest request);
}