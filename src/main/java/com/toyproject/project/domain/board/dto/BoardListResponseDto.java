package com.toyproject.project.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BoardListResponseDto {
    private Long id; // 게시글 ID
    private String title; // 제목
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
    private LocalDateTime startDate; // 시작일
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
    private LocalDateTime endDate; // 종료일
    private String destination; // 여행지
    private Integer maxParticipant; // 최대인원
    private Boolean isClosed; // 마감 여부
    private Integer views; // 조회수
    private Integer likes; // 좋아요 수
    private List<String> tags; // 태그 목록
    private String nickname; // 작성자 닉네임
    private String profileImage; // 작성자 프로필 사진


}
