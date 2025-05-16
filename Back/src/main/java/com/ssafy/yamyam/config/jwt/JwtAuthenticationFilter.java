// ✅ JwtAuthenticationFilter.java - 로그 추가 버전
package com.ssafy.yamyam.config.jwt;

import com.ssafy.yamyam.dto.User;
import com.ssafy.yamyam.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        System.out.println("[JwtFilter] 요청 URI: " + uri);

        if (uri.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("[JwtFilter] 추출된 토큰: " + token);

            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserId(token);
                System.out.println("[JwtFilter] userId from token: " + userId);

                User user = userService.getUserById(userId);
                System.out.println("[JwtFilter] user 조회 결과: " + user);

                if (user != null) {
                    CustomUserDetails userDetails = new CustomUserDetails(user);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("[JwtFilter] 인증 객체 등록 완료");
                } else {
                    System.out.println("[JwtFilter] user 조회 실패 - SecurityContext에 등록 안 함");
                }
            } else {
                System.out.println("[JwtFilter] 유효하지 않은 토큰");
            }
        } else {
            System.out.println("[JwtFilter] Authorization 헤더 없음 또는 형식 오류");
        }

        filterChain.doFilter(request, response);
    }
}
