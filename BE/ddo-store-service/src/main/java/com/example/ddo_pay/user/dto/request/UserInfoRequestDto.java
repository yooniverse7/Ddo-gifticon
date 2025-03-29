package com.example.ddo_pay.user.dto.request;

import lombok.Data;

@Data
public class UserInfoRequestDto {
    private long userId;
    private String userEmail;
    private String phoneNum;
    private String birth;
}
