package com.toyproject.project.domain.member.controller;


import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/my-page")
    public String myPage(@AuthenticationPrincipal Member member) {
        return member.getNickname();
    }


}
