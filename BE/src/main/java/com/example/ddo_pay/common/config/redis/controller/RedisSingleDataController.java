package com.example.ddo_pay.common.config.redis.controller;

import com.example.ddo_pay.common.config.redis.dto.RedisDto;
import com.example.ddo_pay.common.config.redis.service.RedisSingleDataService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * Redis 단일 데이터를 조회, 등록, 삭제하는 로직입니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/redis/singleData")
public class RedisSingleDataController {

    private final RedisSingleDataService redisSingleDataService;

    public RedisSingleDataController(RedisSingleDataService redisSingleDataService) {
        this.redisSingleDataService = redisSingleDataService;
    }

    /**
     * Redis 키를 기반으로 단일 데이터의 값을 조회합니다.
     */
    @PostMapping("/getValue")
    public ResponseEntity<Object> getValue(@RequestBody RedisDto redisDto) {
        String result = redisSingleDataService.getSingleData(redisDto.getKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Redis 단일 데이터 값을 등록/수정합니다.(duration 값이 존재하면 메모리 상 유효시간을 지정합니다.)
     */
    @PostMapping("/setValue")
    public ResponseEntity<Object> setValue(@RequestBody RedisDto redisDto) {
        try {
            int result;
            if (redisDto.getDuration() == null) {
                result = redisSingleDataService.setSingleData(redisDto.getKey(), redisDto.getValue());
            } else {
                result = redisSingleDataService.setSingleData(
                        redisDto.getKey(),
                        redisDto.getValue(),
                        Duration.ofDays(redisDto.getDuration())
                );
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Redis 작업 오류 발생 :: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Redis 연결 실패: " + e.getMessage());
        }
    }

    /**
     * Redis 키를 기반으로 단일 데이터의 값을 삭제합니다.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteRow(@RequestBody RedisDto redisDto) {
        int result = redisSingleDataService.deleteSingleData(redisDto.getKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}