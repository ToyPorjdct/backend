package com.toyproject.project.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardCreateRequestDto {

    private String title; // 제목
    private String description; // 내용
    private Integer maxParticipant; // 최대인원
    private LocalDateTime startDate; // 여행 시작일
    private LocalDateTime endDate; // 여행 종료일
}
