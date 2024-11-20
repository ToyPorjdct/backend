package com.toyproject.project.domain.member.controller;

import com.toyproject.project.domain.member.service.OAuth2JwtHeaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "OAuth2", description = "OAuth2 API")
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final OAuth2JwtHeaderService oAuth2JwtHeaderService;

    @PostMapping("/validate")
    @Operation(summary = "OAuth2 JWT 헤더 설정", description = "OAuth2 JWT 헤더 설정")
    public String oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return oAuth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
    }

}
