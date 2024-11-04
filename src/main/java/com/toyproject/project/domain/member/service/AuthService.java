package com.toyproject.project.domain.member.service;


import com.toyproject.project.domain.member.dto.request.JoinRequest;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.entity.MemberRole;
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
    public Long join(JoinRequest joinRequest) {
        duplicateEmail(joinRequest.getEmail());

        Member member = memberRepository.save(
                Member.builder()
                        .email(joinRequest.getEmail())
                        .password(passwordEncode(joinRequest.getPassword()))
                        .nickname(joinRequest.getNickname())
                        .role(MemberRole.USER)
                        .build()

        );

        return member.getId();
    }

    private String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }

    private void duplicateEmail(String email) {
        if(!memberRepository.findByEmail(email).isEmpty()){
            throw new CustomException(ALREADY_EXIST_MEMBER);
        }
    }


}
