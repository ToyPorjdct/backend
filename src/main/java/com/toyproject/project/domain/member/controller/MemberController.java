package com.toyproject.project.domain.member.controller;


import com.toyproject.project.domain.member.dto.request.UpdateMemberRequest;
import com.toyproject.project.domain.member.dto.response.MyInfoResponse;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.service.MemberService;
import com.toyproject.project.global.login.AuthenticationMember;
import com.toyproject.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member", description = "회원 서비스")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "내정보 조회", description = "로그인한 회원의 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<MyInfoResponse>> myInfo(@AuthenticationMember Member member) {
        return ApiResponse.success(memberService.getMemberInfo(member), "내정보 조회 성공");
    }

    @PatchMapping
    @Operation(summary = "회원정보 수정", description = "회원의 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<String>> updateMember(
            @AuthenticationMember Member member,
            @RequestBody UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(member, updateMemberRequest);
        return ApiResponse.success(null, "회원정보 수정 성공");
    }


}
