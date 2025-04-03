package com.example.ddo_pay.pay.service.impl;

import com.example.ddo_pay.client.BankClient;
import com.example.ddo_pay.common.exception.CustomException;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.common.util.RedisHandler;
import com.example.ddo_pay.pay.dto.bank_request.BankDdoPayChargeRequest;
import com.example.ddo_pay.pay.dto.bank_response.BankChargeResponseDto;
import com.example.ddo_pay.pay.dto.finance.DepositAccountWithdrawRequest;
import com.example.ddo_pay.pay.dto.request.AccountVerifyRequest;
import com.example.ddo_pay.pay.dto.request.ChargeDdoPayRequest;
import com.example.ddo_pay.pay.dto.request.RegisterAccountRequest;
import com.example.ddo_pay.pay.dto.request.RegisterPasswordRequest;
import com.example.ddo_pay.pay.dto.response.GetAccountResponse;
import com.example.ddo_pay.pay.dto.response.GetBalanceResponse;
import com.example.ddo_pay.pay.dto.response.GetPointResponse;
import com.example.ddo_pay.pay.entity.Account;
import com.example.ddo_pay.pay.entity.AssetType;
import com.example.ddo_pay.pay.entity.DdoPay;
import com.example.ddo_pay.pay.entity.History;
import com.example.ddo_pay.pay.finance_api.FinanceClient;
import com.example.ddo_pay.pay.repository.AccountRepository;
import com.example.ddo_pay.pay.repository.DdoPayRepository;
import com.example.ddo_pay.pay.service.PayService;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.service.impl.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PayServiceImpl implements PayService {

    private final RestTemplate restTemplate;
    private final FinanceClient financeClient;
    private final RedisHandler redisHandler;
    private final ObjectMapper objectMapper;
    private final DdoPayRepository ddoPayRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BankClient bankClient;


    // 유효 계좌 인증
    @Override
    public String verifyAccount(Long userId, AccountVerifyRequest request) {
        String accountNumber = request.getAccountNo();
        String randomMemo = generateRandomMemo(); // 랜덤 단어 생성

        DepositAccountWithdrawRequest dto = DepositAccountWithdrawRequest.of(accountNumber, randomMemo);

        ResponseEntity<?> response = financeClient.sendOneWonTransfer(dto);

        if (response == null) {
            return "ERR_API";
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("금융망 응답 실패: " + response.getBody());
            return "ERR_API";
        }

        try {

            JsonNode root = objectMapper.readTree((String) response.getBody());
            String responseCode = root.path("Header").path("responseCode").asText();

            System.out.println("금융망 응답 코드: " + responseCode);

            if ("H0000".equals(responseCode)) {
                String key = "userId:" + userId;
                String value = "word:" + randomMemo + ",accountNo:" + accountNumber;

                redisHandler.executeOperation(() ->
                        redisHandler.getValueOperations().set(key, value, Duration.ofMinutes(6))
                );
                System.out.println("Redis 저장 완료 → key: " + key + " / value: " + value);

            } else if ("A1003".equals(responseCode)) {
                redisHandler.deleteKey("userId:" + userId);
                System.out.println("Redis 키 삭제: userId:" + userId + " (유효하지 않은 계좌)");
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

    // 랜덤 단어 확인 후 계좌 등록
    @Override
    public void registerAccount(Long userId, RegisterAccountRequest request) {
        String inputWord  = request.getRandomWord();
        String redisKey = "userId:" + userId;

        String redisValue = (String) redisHandler.getValueOperations().get(redisKey);
        if (redisValue == null) {
            throw new CustomException(ResponseCode.REDIS_NOT_FOUND);
        }

        System.out.println("입력받은 단어 = " + inputWord);

        String[] parts = redisValue.split(",");
        String storedWord = null;
        String storedAccountNo = null;
        for (String part : parts) {
            if (part.startsWith("word:")) {
                storedWord = part.substring("word:".length());
            } else if (part.startsWith("accountNo:")) {
                storedAccountNo = part.substring("accountNo:".length());
            }
        }

        if (storedWord == null || storedAccountNo == null) {
            throw new CustomException(ResponseCode.INVALID_REDIS_FORMAT);
        }

        // 단어 비교
        if (!storedWord.equalsIgnoreCase(inputWord.trim())) {
            throw new CustomException(ResponseCode.NOT_VERIFIED_ACCOUNT);
        }

        // 사용자 & 또페이 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));

        DdoPay ddoPay = ddoPayRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_DDOPAY));

        // 계좌 저장
        Account account = Account.builder()
                .accountNum(storedAccountNo)
                .ddoPay(ddoPay)
                .build();

        accountRepository.save(account);

        // Redis 삭제
        redisHandler.deleteKey(redisKey);

    }


    // 비밀번호 등록 및 또페이 생성
    @Override
    public void registerPayPassword(Long userId, RegisterPasswordRequest request) {
        String password = request.getPassword();


        // 비밀번호 6자리 숫자인지 검증
        if (!password.matches("^\\d{6}$")) {
            throw new CustomException(ResponseCode.INVALID_PAY_PASSWORD);
        }

        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));

        // 이미 또페이가 등록되어 있다면 예외 처리
        if (ddoPayRepository.existsByUser(user)) {
            throw new CustomException(ResponseCode.ALREADY_REGISTERED_DDOPAY);
        }

        // 또페이 생성
        DdoPay ddoPay = DdoPay.builder()
                .user(user)
                .balance(0)
                .point(0)
                .payPassword(password)
                .build();

        // 양방향 연관 관계 설정
        user.changeDdoPay(ddoPay);

        ddoPayRepository.save(ddoPay);
    }

    // 잔고 조회
    @Override
    public GetBalanceResponse selectBalance(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));
        int balance = findUser.getDdoPay().getBalance();
        return new GetBalanceResponse(balance);
    }

    // 포인트 조회
    @Override
    public GetPointResponse selectPoint(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));
        int point = findUser.getDdoPay().getPoint();
        return new GetPointResponse(point);
    }

    // 계좌 조회
    @Override
    public List<GetAccountResponse> selectAccountList(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_USER));

        // 사용자의 DdoPay 엔티티에서 계좌 목록 가져오기
        List<Account> accountList = findUser.getDdoPay().getAccountList();

        if (accountList.isEmpty()) {
            throw new CustomException(ResponseCode.NO_EXIST_ACCOUNT);
        }

        // 각 Account를 GetAccountResponse로 변환하여 리스트 반환
        return accountList.stream()
                .map(GetAccountResponse::from)
                .collect(Collectors.toList());
    }


    // 기프티콘 생성 시 또페이 잔액 조회 후 출금(잔액 변경)
    @Override
    public void withdrawDdoPay(Long userId, int amount) {
        DdoPay ddoPay = ddoPayRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_DDOPAY));

        if (!ddoPay.isAvailableToPay(amount)) {
            throw new CustomException(ResponseCode.INSUFFICIENT_BALANCE);
        }

        ddoPay.decreaseBalance(amount);
        ddoPayRepository.save(ddoPay);

    }

    // 기프티콘 취소 환불 시 90% 금액 환불
    @Override
    public void depositDdoPay(Long userId, int amount) {
        DdoPay ddoPay = ddoPayRepository.findByUserId(userId).orElseThrow(()
                -> new CustomException(ResponseCode.NO_EXIST_DDOPAY));

        ddoPay.increaseBalance(amount);
        ddoPayRepository.save(ddoPay);

    }

    // 또페이 충전
    @Override
    public void transferDdoPay(Long userId, ChargeDdoPayRequest request) {
        DdoPay ddoPay = ddoPayRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_EXIST_DDOPAY));

        // 비밀번호가 일치하지 않다면,
        if (!ddoPay.checkPassword(request.getPassword())){
            throw new CustomException(ResponseCode.DIFFRENT_PASSWORD);
        }

        // 사용자 계좌 가져오기. 하나만 있다고 가정
        if(ddoPay.getAccountList().isEmpty()) {
            throw new CustomException(ResponseCode.NO_EXIST_ACCOUNT);
        }
        String accountNum = ddoPay.getAccountList().get(0).getAccountNum();

        BankDdoPayChargeRequest bankRequest = BankDdoPayChargeRequest.builder()
                .userAccountNum(accountNum)
                .corporationAccountNum("9990627419918613")
                .amount(request.getAmount())
                .build();

        log.info("Feign 요청 바디: userAccountNum={}, corpAccountNum={}, amount={}",
                bankRequest.getUserAccountNum(),
                bankRequest.getCorporationAccountNum(),
                bankRequest.getAmount()
        );


        ResponseEntity<BankChargeResponseDto> bankResponse = bankClient.chargeDdoPay(bankRequest);

        BankChargeResponseDto responseBody = bankResponse.getBody();
        log.info("Feign 전체 응답 = {}", bankResponse);
        log.info("Feign 응답 바디 = {}", bankResponse.getBody());

        if (responseBody == null || responseBody.getHeader() == null) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "bank", "응답이 비어 있음");
        }

        String responseCode = responseBody.getHeader().getResponseCode();


        // 성공 코드가 오면 balance 변경, history 생성
        if ("H0000".equals(responseCode)) {
            ddoPay.plueBalance(request.getAmount());
            ddoPayRepository.save(ddoPay);

            // History 생성 및 연결
            History history = new History();
            history.setTitle("또페이 충전");
            history.setTime(LocalDateTime.now());
            history.setInOutAmount(request.getAmount());
            history.setType(AssetType.BALANCE);
            history.setDdoPay(ddoPay);

            ddoPay.getHistoryList().add(history);

        } else if ("A1014".equals(responseCode)) {
            throw new CustomException(ResponseCode.INSUFFICIENT_BALANCE, "잔액 부족", "출금 계좌 잔액 부족");
        } else throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR, "bank", "은행 응답 실패 코드: " + responseCode);



    }


}
