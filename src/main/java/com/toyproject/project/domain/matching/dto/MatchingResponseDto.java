package com.toyproject.project.domain.matching.dto;

import com.toyproject.project.domain.matching.domain.Matching;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingResponseDto {
    private Long matchingId;
    private String nickname;
    private Long memberId;

    public static MatchingResponseDto from(Matching matching) {
        return new MatchingResponseDto(
                matching.getId(),
                matching.getMember().getNickname(),
                matching.getMember().getId()
        );
    }
}
