package com.toyproject.project.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentListResponseDto {
        private Long commentId;
        private String content;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm")
        private LocalDateTime createdAt;
        private AuthorResponseDto author;
}
