package com.toyproject.project.domain.member.service;


import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.toyproject.project.global.exception.ErrorCode.ALREADY_EXIST_MEMBER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public void join(JoinRequest joinRequest) {
        duplicateEmail(joinRequest);

        memberRepository.save(
                Member.builder()
                .uuid(UUID.randomUUID().toString())
                .email(joinRequest.getEmail())
                .password(passwordEncode(joinRequest))
                .nickname(joinRequest.getNickname())
                .role("ROLE_USER")
                .build()
        );
    }

    private String passwordEncode(JoinRequest joinRequest) {
        return passwordEncoder.encode(joinRequest.getPassword());
    }

    private void duplicateEmail(JoinRequest joinRequest) {
        if(!memberRepository.findByEmail(joinRequest.getEmail()).isEmpty()){
            throw new CustomException(ALREADY_EXIST_MEMBER);
        }
    }


}
