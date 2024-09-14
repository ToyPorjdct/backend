package com.toyproject.project.global.oauth2.dto;

import com.toyproject.project.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getNickname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRole().equals("ADMIN") ?
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) :
                List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Member getMember() {
        return member;
    }
}
