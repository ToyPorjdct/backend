package com.toyproject.project.global.oauth2.dto;

public interface OAuth2Response {

    // 제공자 (kakao, naver ...)
    String getProvider();

    // 제공자에서 발급해주는 고유아이디
    String getProviderId();

    // 사용자 이메일
    String getEmail();

    // 사용자 실명
    String getName();
}
