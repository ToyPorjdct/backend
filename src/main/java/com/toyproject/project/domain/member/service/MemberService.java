package com.toyproject.project.domain.member.service;

import com.toyproject.project.domain.member.dto.request.UpdateMemberRequest;
import com.toyproject.project.domain.member.dto.response.MyInfoResponse;
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

    public Member getMember(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
    }

    public MyInfoResponse getMemberInfo(Member member){
        return MyInfoResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .role(member.getRole())
                .build();
    }

    @Transactional
    public void updateMember(Member member, UpdateMemberRequest updateMemberRequest){
        member.updateNickname(updateMemberRequest.getNickname());
        memberRepository.save(member);
    }


}
