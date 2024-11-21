package com.toyproject.project.global.config;



import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.oauth2.handler.CustomOAuth2SuccessHandler;
import com.toyproject.project.global.oauth2.service.CustomOauth2UserService;
import com.toyproject.project.global.jwt.filter.JWTFilter;
import com.toyproject.project.global.login.handler.CustomAccessDeniedHandler;
import com.toyproject.project.global.login.handler.CustomAuthenticationEntryPointHandler;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import com.toyproject.project.global.login.filter.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final CustomOauth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

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

        // 필터 예외 처리
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(new CustomAuthenticationEntryPointHandler())
                .accessDeniedHandler(new CustomAccessDeniedHandler()));


        // 필터 관리
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtTokenProvider);
        loginFilter.setFilterProcessesUrl("/auth/login");

        http
                .addFilterBefore(new JWTFilter(jwtTokenProvider, memberRepository), LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);



        // 인가 관리
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**", "/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/board","/board/**").permitAll()
                        .requestMatchers("/member/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // OAuth2 로그인
        http
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/code/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler));


        return http.build();

    }

}
