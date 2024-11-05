package com.toyproject.project.domain.matching.dto;

import com.toyproject.project.domain.matching.domain.Matching;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingResponseDto {
    private Long matchingId;
    private Long memberId;
    private String nickname;


    public static MatchingResponseDto from(Matching matching) {
        return new MatchingResponseDto(
                matching.getId(),
                matching.getMember().getId(),
                matching.getMember().getNickname()
        );
    }
}
