package com.example.ddo_pay.common.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.ddo_pay.common.config.security.token.CustomUserDetails;

@Component
public class SecurityUtil {

    /**
     * 시큐리티 필터에서 검증된 유저의 ID를 받는다
     * 
     * @return
     *         {@code long} userId
     */

    // 현재 인증된 사용자 ID를 반환
    public static Long getUserId() {
        // SecurityContextHolder에서 Authentication 객체를 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 인증된 사용자가 CustomUserDetails 인스턴스일 경우, 그 내부의 userId 반환
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUserId();
        } else {
            // 인증되지 않았거나, CustomUserDetails가 아닌 경우 null 반환
            return null;
        }
    }
}
