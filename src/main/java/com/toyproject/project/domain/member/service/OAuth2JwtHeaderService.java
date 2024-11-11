package com.toyproject.project.domain.member.service;

import com.toyproject.project.global.config.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


@Service
public class OAuth2JwtHeaderService {
    public String oauth2JwtHeaderSet(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if(cookies == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "쿠키가없음";
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization")){
                token = cookie.getValue();
            }
        }

        if(token == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "쿠키안에 토큰이없음";
        }

        // 클라이언트의 access 토큰 쿠키를 만료
        response.addCookie(CookieUtil.createCookie("Authorization", null, 0));
        response.addHeader("Authorization", "Bearer" + token);
        response.setStatus(HttpServletResponse.SC_OK);

        return "success";
    }
}