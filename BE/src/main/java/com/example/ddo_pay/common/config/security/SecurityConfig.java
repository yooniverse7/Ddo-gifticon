package com.example.ddo_pay.common.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // 인증 등록 부분
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // Cors 설정. Webconfig 에서 설정되었다면 기본 설정(withDefault)
                                .cors(Customizer.withDefaults())

                                // Csrf 설정. 뭔지 모름. 끄기(disable)
                                .csrf((cf) -> cf.disable())

                                // basic 세션방식이 아니라 토큰방식이라 끄자고 한다.(disable)
                                .httpBasic((hB) -> hB.disable())

                                // 세션기반이 아님을 선언. 뭔지 모름
                                .sessionManagement((sm) -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // http 요청의 엔드포인트 확인
                                .authorizeHttpRequests((aHR) -> aHR
                                                // 이 경로들은 인증받지 않음
                                                .requestMatchers(
                                                                new AntPathRequestMatcher(
                                                                                "/api/users/social/kakao/login"))
                                                .permitAll()
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                // 그 외의 경로들은 인증
                                                .anyRequest()
                                                .authenticated())
                                .addFilterBefore(new CustomAuthenticationFilter(),
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // Cors 설정 부분
        @Bean
        public CorsConfigurationSource coreConfigurationSource() {
                CorsConfiguration corsconfig = new CorsConfiguration();
                corsconfig.setAllowedOrigins(
                                Arrays.asList("http://j12e106.p.ssafy.io",
                                                "http://j12e106.p.ssafy.io:3000",
                                                "https://j12e106.p.ssafy.io",
                                                "https://j12e106.p.ssafy.io:3000",
                                                "http://localhost:3000"));
                corsconfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsconfig
                                .setAllowedHeaders(Arrays.asList("*", "xx-auth", "content-type", "authorization"));

                // 엔드포인트 설정
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", corsconfig);
                return source;
        }

}
