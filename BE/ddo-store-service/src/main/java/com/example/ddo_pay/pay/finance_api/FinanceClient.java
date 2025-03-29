package com.example.ddo_pay.pay.finance_api;

// 금융망 api 연동 컴포넌트

import com.example.ddo_pay.pay.dto.finance.DepositAccountWithdrawRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Component
@RequiredArgsConstructor
public class FinanceClient {

    private final RestTemplate restTemplate;

    private static final String FINANCE_API_URL =
            "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/updateDemandDepositAccountTransfer";


    // 1원 계좌 이체 api 호출
    public ResponseEntity<String> sendOneWonTransfer(DepositAccountWithdrawRequest dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DepositAccountWithdrawRequest> request = new HttpEntity<>(dto, headers);

        try {
            return restTemplate.postForEntity(FINANCE_API_URL, request, String.class);
        } catch (Exception e) {
            System.out.println("금융망 호출 실패: " + e.getMessage());
            return null;
        }
    }

}
