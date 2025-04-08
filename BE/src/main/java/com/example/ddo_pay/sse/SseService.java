package com.example.ddo_pay.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SseService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 하트비트 주기 (30초)
    private static final long HEARTBEAT_INTERVAL = 30 * 1000L;

    // 요청 주기는 서버가 .send() 호출할 때만 클라이언트로 메시지 전달됨(클라이언트가 주기적으로 요청하지 않음)
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L); // 5분 타임아웃 설정
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId)); // 정상 종료 시 동작
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(userId);
        }); // 타임아웃 시 동작

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 완료"));

            // 하트비트 시작
            startHeartbeat(userId, emitter);

        } catch (IOException e) {
            throw new RuntimeException("SSE 연결 실패", e);
        }
        return emitter;
    }

    private void startHeartbeat(Long userId, SseEmitter emitter) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                if(emitters.containsKey(userId)) {
                    emitter.send(SseEmitter.event()
                            .name("heartbeat")
                            .data("💓")); // 하트비트 메시지
                }
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void sendToUser(Long userId, String eventName, String message) {
        SseEmitter emitter = emitters.get(userId);
        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(message));
            } catch(IOException e) {
                emitters.remove(userId);
                throw new RuntimeException("SSE 전송 실패", e);
            }
        }
    }
}
