// SecurityConfig.java
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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
                .cors()
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/user").authenticated() // ← 이 줄 반드시 필요
                        .requestMatchers(HttpMethod.GET, "/api/user/check-id").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/exists/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/nutrition/**").authenticated()
						.requestMatchers("/api/community").authenticated()
                        .requestMatchers("/api/community/**").authenticated()
						.requestMatchers("/api/comment/user/**").authenticated()
						.requestMatchers("/api/score/**").authenticated()
						.requestMatchers("/api/diet/**").authenticated()
						.requestMatchers("/api/notification/**").authenticated()
						.requestMatchers("/api/food/search").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
