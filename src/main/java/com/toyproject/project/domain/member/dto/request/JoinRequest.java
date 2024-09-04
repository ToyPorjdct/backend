package com.toyproject.project.domain.member.dto.request;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequest {

    private String email;
    private String nickname;
    private String password;

}
