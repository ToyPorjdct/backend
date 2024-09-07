package com.toyproject.project.domain.member.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyInfoResponse {

    private String nickname;
    private String email;
    private String role;
    private String createdAt;
}
