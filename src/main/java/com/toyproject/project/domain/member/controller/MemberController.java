package com.toyproject.project.domain.member.controller;


import com.toyproject.project.domain.member.dto.response.MyPageResponse;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.service.MemberService;
import com.toyproject.project.global.jwt.AuthenticationMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my-page")
    public MyPageResponse myPage(@AuthenticationMember Member member) {
        return memberService.getMyPage(member);
    }


}
