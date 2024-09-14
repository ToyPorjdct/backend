package com.toyproject.project.domain.member.service;

import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class MemberDtoServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원정보 조회")
    void myPage() {
        // given
        JoinRequest joinRequest = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test")
                .password("1234")
                .build();

        authService.join(joinRequest);

        // when
        Member member = memberRepository.findByEmailOrElseThrow(joinRequest.getEmail());
        memberService.getMemberInfo(member);

        // then
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getNickname()).isEqualTo("test");

    }
}
