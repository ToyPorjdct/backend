package com.toyproject.project.global.oauth2.service;

import com.toyproject.project.global.oauth2.dto.CustomOAuth2User;
import com.toyproject.project.global.oauth2.dto.KakaoResponse;
import com.toyproject.project.global.oauth2.dto.OAuth2Response;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    /**
     * 소셜 로그인 성공시 후처리
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        // 소셜 로그인을 통해 받은 정보
        OAuth2User oAuth2User = super.loadUser(request);

        OAuth2Response oauth2Response = null;
        String socialCode = null;

        if (request.getClientRegistration().getClientName().equals("kakao")) {
            oauth2Response = new KakaoResponse(oAuth2User.getAttributes());
            socialCode = oauth2Response.getProvider() + "_" + oauth2Response.getProviderId();

        } else {
            log.error("지원하지 않는 소셜 로그인입니다");
        }

        Optional<Member> member = memberRepository.findByEmailAndSocialCode(oauth2Response.getEmail(), socialCode);

        if(member.isEmpty()) {
            Optional<Member> memberByEmail = memberRepository.findByEmail(oauth2Response.getEmail());
            if(memberByEmail.isPresent()) {
                // 이메일이 이미 존재할경우 기존 회원에 소셜코드만 추가해서 가입
                memberByEmail.get().updateSocialCode(socialCode);
                return new CustomOAuth2User(memberRepository.save(memberByEmail.get()), oAuth2User.getAttributes());
            }
            Member savedMember = registerNewMember(oauth2Response, socialCode);
            return new CustomOAuth2User(savedMember, oAuth2User.getAttributes());
        }


        return new CustomOAuth2User(member.get(), oAuth2User.getAttributes());
    }

    private Member registerNewMember(OAuth2Response oauth2Response, String socialCode) {
        Member savedMember = memberRepository.save(
                Member.builder()
                        .email(oauth2Response.getEmail())
                        .nickname(oauth2Response.getName())
                        .password(UUID.randomUUID().toString())
                        .socialCode(socialCode)
                        .role("ROLE_USER")
                        .build()
        );
        return savedMember;
    }



}
