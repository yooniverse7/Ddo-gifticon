package com.example.ddo_pay.common.util.redis.controller;

import com.example.ddo_pay.common.util.redis.dto.RedisDto;
import com.example.ddo_pay.common.util.redis.service.RedisSingleDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/v1/redis")
@RequiredArgsConstructor
public class RedisSingleDataController  {

    private final RedisSingleDataService redisService;

    /**
     * Redis 단일 데이터 저장 또는 수정 (duration 지정 가능)
     */
    @PostMapping
    public ResponseEntity<?> setValue(@RequestBody RedisDto dto) {
        try {
            int result = (dto.getDuration() == null)
                    ? redisService.setSingleData(dto.getKey(), dto.getValue())
                    : redisService.setSingleData(dto.getKey(), dto.getValue(), Duration.ofSeconds(dto.getDuration()));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Redis 저장 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Redis 저장 실패: " + e.getMessage());
        }
    }

    /**
     * Redis 단일 데이터 조회
     */
    @GetMapping("/{key}")
    public ResponseEntity<?> getValue(@PathVariable String key) {
        String result = redisService.getSingleData(key);
        return ResponseEntity.ok(result);
    }

    /**
     * Redis 단일 데이터 삭제
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<?> deleteValue(@PathVariable String key) {
        int result = redisService.deleteSingleData(key);
        return ResponseEntity.ok(result);
    }
}
