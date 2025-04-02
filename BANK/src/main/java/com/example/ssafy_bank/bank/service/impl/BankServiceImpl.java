package com.example.ssafy_bank.bank.service.impl;

import com.example.ssafy_bank.bank.dto.finance_request.CreateAccountRequestDto;
import com.example.ssafy_bank.bank.dto.finance_request.CreateUserKeyRequestDto;
import com.example.ssafy_bank.bank.dto.finance_response.CreateAccountResponseDto;
import com.example.ssafy_bank.bank.dto.finance_response.CreateUserKeyResponseDto;
import com.example.ssafy_bank.bank.entity.SsafyUser;
import com.example.ssafy_bank.bank.repository.BankRepository;
import com.example.ssafy_bank.bank.service.BankService;
import com.example.ssafy_bank.common.exception.CustomException;
import com.example.ssafy_bank.common.response.ResponseCode;
import com.example.ssafy_bank.config.BankApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final WebClient bankWebClient;
    private final BankApiConfig bankApiConfig;


    // 사용자 금융망 계정 + userKey 생성
    @Override
    public void createUserKey(String email) {
        Optional<SsafyUser> existingUser = bankRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            log.info("이미 존재하는 사용자: {}", email);
            throw new CustomException(ResponseCode.ALREADY_EXIST_USER, "email", "이미 존재하는 이메일입니다.");
        }

        CreateUserKeyRequestDto request = CreateUserKeyRequestDto.builder()
                .apiKey(bankApiConfig.getApiKey())
                .userId(email)
                .build();

        CreateUserKeyResponseDto response = bankWebClient.post()
                .uri("/member/")
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateUserKeyResponseDto.class)
                .doOnNext(res -> log.info("성공 응답: {}", res))
                .block();

        String userKey = Objects.requireNonNull(response).getUserKey();

        // 받아온 userKey로 계좌 생성
        CreateAccountRequestDto accountRequest = CreateAccountRequestDto.of(userKey, bankApiConfig.getApiKey());

        CreateAccountResponseDto accountResponse = bankWebClient.post()
                .uri("/edu/demandDeposit/createDemandDepositAccount")
                .header("Content-Type", "application/json")
                .bodyValue(accountRequest)
                .retrieve()
                .bodyToMono(CreateAccountResponseDto.class)
                .doOnNext(res -> log.info("계좌 생성 응답: {}", res))
                .doOnError(e -> log.error("계좌 생성 중 에러 발생: {}", e.getMessage()))
                .block();

        if (Objects.requireNonNull(accountResponse).getHeader() == null) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "header", "응답 헤더가 비어 있음");
        }

        log.info("전체 계좌 생성 응답 객체: {}", accountResponse);


        String responseCode = Objects.requireNonNull(accountResponse).getHeader().getResponseCode();
        log.info("계좌생성 응답 코드 : {}", responseCode);
        if(!"H0000".equals(responseCode)) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "account", "계좌 생성 실패");
        }

        String accountNo = accountResponse.getRec().getAccountNo();

        SsafyUser user = SsafyUser.builder()
                .accountNum(accountNo)
                .userKey(Objects.requireNonNull(response).getUserKey())
                .email(email)
                .build();

        bankRepository.save(user);
    }



}
