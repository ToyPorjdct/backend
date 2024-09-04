package com.toyproject.project.domain.member.service;


import com.toyproject.project.domain.member.dto.request.LoginRequest;
import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.dto.response.TokenResponse;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 회원가입
     */
    @Transactional
    public void join(JoinRequest signupRequest) {
        if(!memberRepository.findByEmail(signupRequest.getEmail()).isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일입니다");
        }

        memberRepository.save(
                Member.builder()
                .uuid(UUID.randomUUID().toString())
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .build()
        );
    }


    /**
     * 로그인
     */
    public TokenResponse login(LoginRequest loginRequest){
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new IllegalStateException("아아디 또는 비밀번호가 일치하지 않습니다"));

        String token = jwtTokenProvider.createToken(member.getUuid());

        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
