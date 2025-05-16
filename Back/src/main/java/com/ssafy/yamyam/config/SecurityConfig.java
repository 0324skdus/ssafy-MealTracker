package com.ssafy.yamyam.config;

import com.ssafy.yamyam.config.jwt.JwtAuthenticationFilter;
import com.ssafy.yamyam.config.jwt.JwtUtil;
import com.ssafy.yamyam.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public SecurityConfig(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll() // 회원가입 허용
                        .requestMatchers(HttpMethod.GET, "/api/user/exists/**").permitAll() // ID 중복확인 허용
                        .requestMatchers("/api/auth/**").permitAll() // 로그인 관련 허용
                        .requestMatchers("/api/community").permitAll()
                        .requestMatchers("/api/community/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
