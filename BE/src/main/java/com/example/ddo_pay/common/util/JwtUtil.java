package com.example.ddo_pay.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

public class JwtUtil {
    // 비밀 키 (서명용) - 실제 프로젝트에서는 외부에서 관리해야 합니다.
    private static final String SECRET_STRING = "your-secret-key";
    private SecretKey secretKey;

    @PostConstruct
    public void createSecretKey() {
        // 키 생성
        this.secretKey = new SecretKeySpec(SECRET_STRING.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT 토큰 생성 및 서명
    public String generateToken(String tokenName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(tokenName, claims); // 추가적인 정보가 필요하면 claims에 데이터를 넣으면 됩니다.
    }

    // JWT 토큰 검증
    public boolean validateToken(String token, String username) {
        // 사용자명과 토큰에 담긴 사용자명이 일치하는지, 만료되지 않았는지 확인
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    // JWT 생성 메소드
    private String createToken(String username, Map<String, Object> claims) {
        // 토큰의 유효기간을 1시간으로 설정
        long expirationTime = 1000 * 60 * 60; // 1시간

        // JWT 생성
        return Jwts.builder()
                .claims(claims) // Claims에 사용자 정보 등을 넣을 수 있습니다.
                .subject(username) // 토큰의 주체는 사용자명
                .issuedAt(new Date()) // 토큰 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간
                .signWith(secretKey) // 서명 알고리즘과 비밀 키
                .compact(); // JWT 반환
    }

    // JWT에서 Claims 추출 메소드
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명 검증을 위한 비밀 키 설정
                .build()
                .parseSignedClaims(token) // JWT 파싱
                .getPayload(); // Claims 추출
    }

    // JWT에서 사용자명 추출
    private String extractUsername(String token) {
        return extractClaims(token).getSubject(); // Claims에서 Subject(사용자명) 반환
    }

    // JWT 토큰의 유효성 검사 (Token을 검증 후 유효한지 반환)
    private boolean isTokenValid(String token) {
        try {
            // 검증하는 메소드
            return !isTokenExpired(token);
        } catch (Exception e) {
            // 토큰이 만료되었거나 잘못된 경우 예외 발생
            return false;
        }
    }

    // JWT 토큰의 유효성 검사
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date()); // 만료된 토큰인지 확인
    }
}
