package com.toyproject.project.domain.member.service;

import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.dto.request.LoginRequest;
import com.toyproject.project.domain.member.dto.response.TokenResponse;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원가입 성공")
    void join() {
        // given
        JoinRequest joinRequest = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test")
                .password("1234")
                .build();

        // when
        authService.join(joinRequest);

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("중복 회원")
    void join_duplicate() {
        // given
        JoinRequest joinRequest1 = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test2")
                .password("1234")
                .build();

        JoinRequest joinRequest2 = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test2")
                .password("1234")
                .build();

        // when
        authService.join(joinRequest1);

        // then
        assertThatThrownBy(() -> authService.join(joinRequest2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void login() {
        // given
        JoinRequest joinRequest1 = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test2")
                .password("1234")
                .build();

        authService.join(joinRequest1);


        // when
        TokenResponse token = authService.login(
                LoginRequest.builder()
                        .email(joinRequest1.getEmail())
                        .password(joinRequest1.getPassword())
                        .build());

        //then
        assertThat(jwtTokenProvider.validateToken(token.getToken())).isTrue();
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 불일치")
    void login_fail() {
        // given
        JoinRequest joinRequest1 = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test2")
                .password("1234")
                .build();

        authService.join(joinRequest1);

        //then
        LoginRequest loginRequest = LoginRequest.builder()
                .email(joinRequest1.getEmail())
                .password("123")
                .build();

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalStateException.class);
    }
}