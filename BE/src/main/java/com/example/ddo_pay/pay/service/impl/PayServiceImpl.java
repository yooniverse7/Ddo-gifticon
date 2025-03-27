package com.example.ddo_pay.pay.service.impl;

import com.example.ddo_pay.common.config.redis.handler.RedisHandler;
import com.example.ddo_pay.pay.dto.finance.DepositAccountWithdrawRequestDto;
import com.example.ddo_pay.pay.dto.request.AccountVerifyRequest;
import com.example.ddo_pay.pay.finance_api.FinanceClient;
import com.example.ddo_pay.pay.service.PayService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final RestTemplate restTemplate;
    private final FinanceClient financeClient;
    private final RedisHandler redisHandler;
    private final ObjectMapper objectMapper;

    // 유효 계좌 인증
    @Override
    public String verifyAccount(Long userId, AccountVerifyRequest request) {
        String accountNumber = request.getAccountNo();
        String randomMemo = generateRandomMemo(); // 랜덤 단어 생성

        DepositAccountWithdrawRequestDto dto
                = DepositAccountWithdrawRequestDto.of(accountNumber, randomMemo);

        // 금융망에 post 요청
        ResponseEntity<?> response = financeClient.sendOneWonTransfer(dto);

        if(response == null || !response.getStatusCode().is2xxSuccessful()) {
            return "ERR_API";
        }

        try {
            JsonNode root = objectMapper.readTree((JsonParser) response.getBody());
            String responseCode = root.path("Header").path("responseCode").asText();

            if("H0000".equals(responseCode)) {
                // 레디스 저장
                String key = "userId:" + userId;
                String value = "word:" + randomMemo + ",accountNo:" + accountNumber;

                redisHandler.executeOperation(() ->
                    redisHandler.getValueOperations().set(key, value, Duration.ofMinutes(6))
                );


            } else if ("A1003".equals(responseCode)) {
                redisHandler.deleteKey("userId:" + userId);
            }
            return responseCode;


        } catch (IOException e) {
            return "ERR_PARSING";
        }

    }


    // 랜덤 영단어 api 호출
    private String generateRandomMemo() {
        String[] response = restTemplate.getForObject("https://random-word-api.herokuapp.com/word", String[].class);
        return (response != null && response.length > 0) ? response[0] : "default";
    }


}
