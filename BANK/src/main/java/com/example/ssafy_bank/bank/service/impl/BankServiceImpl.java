package com.example.ssafy_bank.bank.service.impl;

import com.example.ssafy_bank.bank.dto.ddopay_request.ChargeDdoPayRequestDto;
import com.example.ssafy_bank.bank.dto.ddopay_response.BankChargeResponseDto;
import com.example.ssafy_bank.bank.dto.finance_request.*;
import com.example.ssafy_bank.bank.dto.finance_response.CreateAccountResponseDto;
import com.example.ssafy_bank.bank.dto.finance_response.CreateUserKeyResponseDto;
import com.example.ssafy_bank.bank.dto.finance_response.SelectBalanceResponseDto;

import com.example.ssafy_bank.bank.dto.finance_response.SelectHistoryResponseDto;
import com.example.ssafy_bank.bank.dto.request.TransactionSummaryDto;
import com.example.ssafy_bank.bank.dto.response.BalanceResponseDto;
import com.example.ssafy_bank.bank.dto.response.LoginResponseDto;
import com.example.ssafy_bank.bank.entity.SsafyUser;
import com.example.ssafy_bank.bank.repository.BankRepository;
import com.example.ssafy_bank.bank.service.BankService;
import com.example.ssafy_bank.common.exception.CustomException;
import com.example.ssafy_bank.common.exception.SuccessfulAccountCreationException;
import com.example.ssafy_bank.common.response.ResponseCode;
import com.example.ssafy_bank.config.BankApiConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        log.info("CreateUserKey 메서드 시작");

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
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(body -> {
                            log.warn("400 Bad Request 응답 본문: {}", body);
                            try {
                                CreateAccountResponseDto dto = new ObjectMapper().readValue(body, CreateAccountResponseDto.class);
                                if ("H0000".equals(dto.getHeader().getResponseCode())) {
                                    return Mono.<Throwable>error(new SuccessfulAccountCreationException(dto));
                                } else {
                                    return Mono.<Throwable>error(new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "account", "계좌 생성 실패"));
                                }
                            } catch (Exception ex) {
                                return Mono.<Throwable>error(new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "account", "계좌 생성 실패 - 응답 파싱 에러"));
                            }
                        })
                )
                .bodyToMono(CreateAccountResponseDto.class)
                .doOnNext(res -> log.info("계좌 생성 응답: {}", res))
                .onErrorResume(e -> {
                    if (e instanceof SuccessfulAccountCreationException) {
                        return Mono.just(((SuccessfulAccountCreationException) e).getDto());
                    }
                    return Mono.error(e);
                })
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

        // 1000만원 입금하기
        Deposit1000RequestDto depositRequest = Deposit1000RequestDto.of(accountNo, bankApiConfig.getApiKey());

        bankWebClient.post()
                .uri("/edu/demandDeposit/updateDemandDepositAccountDeposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(depositRequest)
                .retrieve()

                .bodyToMono(String.class)  // 입금 응답은 추후 DTO로 매핑할 수 있음
                .doOnNext(res -> log.info("입금 응답: {}", res))
                .block();

        log.info("CreateUserKey 메서드 종료");
    }


    // 이메일 로그인
    @Override
    public LoginResponseDto login(String email) {
        Optional<SsafyUser> existingUser = bankRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            SsafyUser user = existingUser.get();
            log.info("로그인 성공: {}", email);
            return new LoginResponseDto(user.getUserId(), user.getEmail());

        } else {
            log.error("존재하지 않는 이메일: {}", email);
            throw new CustomException(ResponseCode.USER_NOT_FOUND, "email", "존재하지 않는 이메일입니다.");
        }
    }

    // 계좌 내역 조회
    @Override
    public List<TransactionSummaryDto> selectHistory(Long userId) {
        Optional<SsafyUser> userOpt = bankRepository.findById(userId);
        if(userOpt.isEmpty()) {
            throw new CustomException(ResponseCode.USER_NOT_FOUND);
        }
        SsafyUser user = userOpt.get();

        SelectHistoryRequestDto requestDto = SelectHistoryRequestDto.of(
                bankApiConfig.getApiKey(),
                user.getUserKey(),
                user.getAccountNum()
        );

        SelectHistoryResponseDto historyResponse = bankWebClient.post()
                .uri("/edu/demandDeposit/inquireTransactionHistoryList")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(SelectHistoryResponseDto.class)
                .doOnNext(res -> log.info("전체 거래 내역 응답: {}", res))
                .block();

        if(historyResponse == null || historyResponse.getHeader() == null) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        String responseCode = historyResponse.getHeader().getResponseCode();
        log.info("계좌내역 응답 코드: {}", responseCode);
        if (!"H0000".equals(responseCode)) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "history", "거래 내역 조회 실패");
        }

        List<TransactionSummaryDto> summaries = historyResponse.getRec().getList().stream()
                .map(detail -> new TransactionSummaryDto(
                        detail.getTransactionTypeName(),
                        detail.getTransactionAfterBalance(),
                        detail.getTransactionDate(),
                        detail.getTransactionTime()
                ))
                .collect(Collectors.toList());

        return summaries;
    }

    // 잔액 조회
    @Override
    public BalanceResponseDto getBalance(Long userId) {
        Optional<SsafyUser> userOpt = bankRepository.findById(userId);
        if(userOpt.isEmpty()) {
            throw new CustomException(ResponseCode.USER_NOT_FOUND);
        }
        SsafyUser user = userOpt.get();

        SelectBalanceRequestDto requestDto = SelectBalanceRequestDto.of(
                bankApiConfig.getApiKey(),
                user.getUserKey(),
                user.getAccountNum()
        );
        SelectBalanceResponseDto balanceResponse = bankWebClient.post()
                .uri("/edu/demandDeposit/inquireDemandDepositAccountBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(SelectBalanceResponseDto.class)
                .doOnNext(res -> log.info("잔액 조회 응답: {}", res))
                .block();

        if (balanceResponse == null || balanceResponse.getRec() == null) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "balance", "잔액 조회 실패");
        }
        String balance = balanceResponse.getRec().getAccountBalance();

        return new BalanceResponseDto(balance);
    }



    // 또페이 계좌이체 요청
    @Override
    public BankChargeResponseDto chargeDdoPay(ChargeDdoPayRequestDto request) {

        log.info("받은 userAccountNum = {}", request.getUserAccountNum());
        log.info("받은 corporationAccountNum = {}", request.getCorporationAccountNum());
        log.info("받은 amount = {}", request.getAmount());

        String userAccount = request.getUserAccountNum().trim();
        log.info("요청받은 userAccountNum: '{}'", userAccount);
        SsafyUser user = bankRepository.findByAccountNum(userAccount)
                .orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<SsafyUser> allUsers = bankRepository.findAll();
        for (SsafyUser u : allUsers) {
            log.info("DB에 저장된 계좌번호: '{}'", u.getAccountNum());
        }


        DdoPayDepositRequestDto depositRequest = DdoPayDepositRequestDto.of(
                request.getUserAccountNum(),
                bankApiConfig.getApiKey(),
                request.getCorporationAccountNum(),
                request.getAmount(),
                user.getUserKey()
        );


        // 금융망 호출
        Map<String, Object> bankResponse = bankWebClient.post()
                .uri("/edu/demandDeposit/updateDemandDepositAccountTransfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(depositRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        Map<String, Object> headerRaw = (Map<String, Object>) bankResponse.get("Header");


        BankChargeResponseDto.BankChargeHeader headerDto = BankChargeResponseDto.BankChargeHeader.builder()
                .responseCode(String.valueOf(headerRaw.get("responseCode")))
                .responseMessage(String.valueOf(headerRaw.get("responseMessage")))
                .apiName(String.valueOf(headerRaw.get("apiName")))
                .transmissionDate(String.valueOf(headerRaw.get("transmissionDate")))
                .transmissionTime(String.valueOf(headerRaw.get("transmissionTime")))
                .institutionCode(String.valueOf(headerRaw.get("institutionCode")))
                .apiKey(String.valueOf(headerRaw.get("apiKey")))
                .apiServiceCode(String.valueOf(headerRaw.get("apiServiceCode")))
                .institutionTransactionUniqueNo(String.valueOf(headerRaw.get("institutionTransactionUniqueNo")))
                .build();

        // REC 목록 파싱
        List<Map<String, Object>> recRawList = (List<Map<String, Object>>) bankResponse.get("REC");

        List<BankChargeResponseDto.TransactionRecord> recList = recRawList.stream()
                .map(r -> BankChargeResponseDto.TransactionRecord.builder()
                        .transactionUniqueNo(String.valueOf(r.get("transactionUniqueNo")))
                        .accountNo(String.valueOf(r.get("accountNo")))
                        .transactionDate(String.valueOf(r.get("transactionDate")))
                        .transactionType(String.valueOf(r.get("transactionType")))
                        .transactionTypeName(String.valueOf(r.get("transactionTypeName")))
                        .transactionAccountNo(String.valueOf(r.get("transactionAccountNo")))
                        .build())
                .toList();


        // 최종 응답 DTO 생성
        BankChargeResponseDto responseDto = BankChargeResponseDto.builder()
                .header(headerDto)
                .rec(recList)
                .build();

        log.info("최종 BankChargeResponseDto = {}", responseDto);
        return responseDto;

    }


}
