package com.toyproject.project.global.config;


import com.toyproject.project.domain.member.service.MemberService;
import com.toyproject.project.global.jwt.JwtFilter;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable());

        // 권한 관리
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/*").permitAll()
                        .requestMatchers("/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 필터 관리
        http
                .addFilterBefore(new JwtFilter(jwtTokenProvider, memberService), UsernamePasswordAuthenticationFilter.class);



        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
