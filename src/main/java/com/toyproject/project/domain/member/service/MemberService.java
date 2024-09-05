package com.toyproject.project.domain.member.service;

import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMember(String uuid){
        return memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
    }


}
