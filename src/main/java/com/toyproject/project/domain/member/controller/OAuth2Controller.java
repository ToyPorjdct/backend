package com.toyproject.project.domain.member.controller;

import com.toyproject.project.domain.member.service.OAuth2JwtHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final OAuth2JwtHeaderService oAuth2JwtHeaderService;

    @PostMapping("/validate")
    public String oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return oAuth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
    }

}
