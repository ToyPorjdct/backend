package com.toyproject.project.domain.participant.dto;

import com.toyproject.project.domain.participant.domain.Participant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantResponseDto {
    private Long participantId;
    private String nickname;
    private Long memberId;

    public static ParticipantResponseDto from(Participant participant) {
        return new ParticipantResponseDto(
                participant.getId(),
                participant.getMember().getNickname(),
                participant.getMember().getId()
        );
    }
}
