package com.toyproject.project.global.oauth2.handler;

import com.toyproject.project.global.jwt.JwtTokenProvider;
import com.toyproject.project.global.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
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

        String url = makeRedirectUrl(jwtTokenProvider.createToken(memberId));

        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String token) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000")
                .queryParam("token", token)
                .build().toUriString();
    }


}
