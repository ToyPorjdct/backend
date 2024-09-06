package com.toyproject.project.domain.member.controller;

import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest) {
        authService.join(joinRequest);
        return "회원가입 성공";
    }




}
