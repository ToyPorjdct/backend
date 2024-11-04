package com.toyproject.project.domain.board.dto;

import com.toyproject.project.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardDetailResponseDto {
        private Long id;
        private String title;
        private String description;
        private String memberNickname;
        private Integer maxParticipant;
        private Integer currentParticipant;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;

        public static BoardDetailResponseDto from(Board board) {
                return new BoardDetailResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getDescription(),
                        board.getMember().getNickname(),
                        board.getMaxParticipant(),
                        board.getCurrentParticipant(),
                        board.getStartDate(),
                        board.getEndDate(),
                        board.getCreatedAt()
                );
        }
}
