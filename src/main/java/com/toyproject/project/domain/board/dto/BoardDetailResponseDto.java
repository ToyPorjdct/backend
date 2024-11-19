package com.toyproject.project.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toyproject.project.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardDetailResponseDto {
        private Long id;
        private String title;
        private String description;
        private Integer maxParticipant;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
        private LocalDate startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
        private LocalDate endDate;
        private Boolean isClosed;
        private Integer views;
        private Integer likes;
        private String destination;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd HH:mm")
        private LocalDateTime createdAt;
        private List<String> tags; // 태그 목록
        private AuthorResponseDto author;


}
