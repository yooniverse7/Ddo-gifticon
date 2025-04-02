package com.example.ssafy_bank.bank.dto.finance_response;

import lombok.Data;


// 사용자 계정 생성 시 응답받는 dto
@Data
public class CreateUserKeyResponseDto {
    private String userId;
    private String userName;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;
}
