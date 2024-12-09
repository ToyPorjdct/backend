package com.toyproject.project.domain.member.service;

import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.exception.CustomException;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.toyproject.project.global.exception.ErrorCode.ALREADY_EXIST_MEMBER;
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

    @Autowired
    private MemberService memberService;


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
        Member member = memberRepository.findByEmail(joinRequest.getEmail()).get();
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("회원가입 실패 : 중복 회원")
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
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("ErrorCode", ALREADY_EXIST_MEMBER);
    }

}