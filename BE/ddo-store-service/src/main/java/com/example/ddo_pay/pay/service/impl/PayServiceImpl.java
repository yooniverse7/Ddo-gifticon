package com.example.ddo_pay.pay.service.impl;

import com.example.ddo_pay.common.exception.CustomException;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.common.util.RedisHandler;
import com.example.ddo_pay.pay.dto.finance.DepositAccountWithdrawRequest;
import com.example.ddo_pay.pay.dto.request.AccountVerifyRequest;
import com.example.ddo_pay.pay.dto.request.RegisterAccountRequest;
import com.example.ddo_pay.pay.dto.request.RegisterPasswordRequest;
import com.example.ddo_pay.pay.dto.response.GetAccountResponse;
import com.example.ddo_pay.pay.dto.response.GetBalanceResponse;
import com.example.ddo_pay.pay.dto.response.GetPointResponse;
import com.example.ddo_pay.pay.entity.Account;
import com.example.ddo_pay.pay.entity.DdoPay;
import com.example.ddo_pay.pay.finance_api.FinanceClient;
import com.example.ddo_pay.pay.repository.AccountRepository;
import com.example.ddo_pay.pay.repository.DdoPayRepository;
import com.example.ddo_pay.pay.service.PayService;
import com.example.ddo_pay.user.entity.User;
import com.example.ddo_pay.user.service.impl.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final RestTemplate restTemplate;
    private final FinanceClient financeClient;
    private final RedisHandler redisHandler;
    private final ObjectMapper objectMapper;
    private final DdoPayRepository ddoPayRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


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
            throw new CustomException(ResponseCode.NO_EXIST_ACCOUNT); // NO_EXIST_ACCOUNT 코드가 ResponseCode에 정의되어 있어야 합니다.
        }

        // 각 Account를 GetAccountResponse로 변환하여 리스트 반환
        return accountList.stream()
                .map(GetAccountResponse::from)
                .collect(Collectors.toList());
    }



}
