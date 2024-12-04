package com.toyproject.project.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthorResponseDto {
    private Long id;
    private String nickname;
    private String profileImage;
}
