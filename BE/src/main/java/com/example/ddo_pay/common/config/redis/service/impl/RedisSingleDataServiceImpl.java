package com.example.ddo_pay.common.config.redis.service.impl;

import com.example.ddo_pay.common.config.redis.handler.RedisHandler;
import com.example.ddo_pay.common.config.redis.service.RedisSingleDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisSingleDataServiceImpl implements RedisSingleDataService {

    private final RedisHandler redisHandler;

    @Override
    public int setSingleData(String key, Object value) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value));
    }

    @Override
    public int setSingleData(String key, Object value, Duration duration) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value, duration));
    }

    @Override
    public String getSingleData(String key) {
        Object value = redisHandler.getValueOperations().get(key);
        return value != null ? value.toString() : "";
    }

    @Override
    public int deleteSingleData(String key) {
        return redisHandler.executeOperation(() -> redisHandler.deleteKey(key));
    }
}
