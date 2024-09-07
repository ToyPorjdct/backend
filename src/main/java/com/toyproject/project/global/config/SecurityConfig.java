package com.toyproject.project.global.config;



import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.filter.JWTFilter;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import com.toyproject.project.global.filter.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic((auth) -> auth.disable());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 필터 관리

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtTokenProvider);
        loginFilter.setFilterProcessesUrl("/auth/login");

        http
                .addFilterBefore(new JWTFilter(jwtTokenProvider, memberRepository), LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);


        // 인가 관리
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/*").permitAll()
                        .requestMatchers("/member/*").hasRole("USER")
                        .requestMatchers("/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated());


        return http.build();

    }

}
