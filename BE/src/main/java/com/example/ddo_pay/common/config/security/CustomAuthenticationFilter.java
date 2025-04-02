package com.example.ddo_pay.common.config.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ddo_pay.common.config.security.token.CustomAuthToken;
import com.example.ddo_pay.common.config.security.token.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("token check filter loading");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // "/api/users/social/kakao/login"을 제외한 모든 요청 처리
        if (!request.getRequestURI().equals("/api/users/social/kakao/login")) {
            String token = request.getHeader("XX-Auth");

            log.info("its : " + token);
            if ("acc-tkn".equals(token)) {
                // 토큰이 valid하면 유저 정보 설정 (예: userId = 32)
                CustomUserDetails userDetails = new CustomUserDetails((long) 1);
                CustomAuthToken authToken = new CustomAuthToken(userDetails, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else if ("acc-tkn2".equals(token)) {
                // 토큰이 valid하면 유저 정보 설정 (예: userId = 32)
                CustomUserDetails userDetails = new CustomUserDetails((long) 2);
                CustomAuthToken authToken = new CustomAuthToken(userDetails, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response); // 필터 체인 계속 진행
    }
}
