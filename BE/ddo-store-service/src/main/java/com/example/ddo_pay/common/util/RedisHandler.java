package com.example.ddo_pay.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j // 로그 객체 자동 생성
@Component
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    public ListOperations<String, Object> getListOperations() {
        return redisTemplate.opsForList();
    }

    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public ValueOperations<String, Object> getValueOperations() {
        return redisTemplate.opsForValue();
    }

    public int executeOperation(Runnable operation) {
        try {
            operation.run();
            return 1;
        } catch (Exception e) {
            log.error("Redis 작업 오류 발생", e);
            return 0;
        }
    }
}