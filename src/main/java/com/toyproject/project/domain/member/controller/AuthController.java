package com.toyproject.project.domain.member.controller;

import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.dto.request.LoginRequest;
import com.toyproject.project.domain.member.service.AuthService;
import com.toyproject.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody JoinRequest joinRequest) {
        authService.join(joinRequest);
        return ApiResponse.success(null, "회원 가입 성공");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "스웨거 등록을 위한 컨트롤러, 실제 로그인은 필터에서 처리")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.success(null, "로그인 성공");
    }



}
