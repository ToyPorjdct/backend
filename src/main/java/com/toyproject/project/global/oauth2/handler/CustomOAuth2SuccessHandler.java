package com.toyproject.project.global.oauth2.handler;

import com.toyproject.project.global.jwt.JwtTokenProvider;
import com.toyproject.project.global.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;


    /**
     * OAuth2 로그인 성공시 JWT 토큰 생성 후 리다이렉트
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공");

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customUserDetails.getMember().getId();

        String token = jwtTokenProvider.createToken(memberId);

        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:3000/oauth2/redirect");

    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }




}
