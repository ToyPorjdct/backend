package com.toyproject.project.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toyproject.project.domain.board.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardCreateRequestDto {

    private String title; // 제목
    private String description; // 내용
    private String destination; // 여행지
    private Integer maxParticipant; // 최대인원
    private LocalDate startDate; // 여행 시작일
    private LocalDate endDate; // 여행 종료일
    private List<String> tagNames;
}
