package com.example.ddo_pay.common.config.redis.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisDto {
    private String key; // 저장할 키
    private String value; // 저장할 값
    private Long duration; // 유효 시간(초), null이면 무제한. 60L == 60초
}
