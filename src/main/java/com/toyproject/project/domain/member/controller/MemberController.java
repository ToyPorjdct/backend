package com.toyproject.project.domain.member.controller;


import com.toyproject.project.domain.member.dto.response.MyInfoResponse;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.service.MemberService;
import com.toyproject.project.global.jwt.AuthenticationMember;
import com.toyproject.project.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my-info")
    public ResponseEntity<ApiResponse<MyInfoResponse>> myInfo(@AuthenticationMember Member member) {
        return ApiResponse.success(memberService.getMemberInfo(member), "회원정보 조회 성공");
    }


}
