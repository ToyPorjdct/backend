package com.toyproject.project.domain.participant.controller;


import com.toyproject.project.domain.participant.dto.ParticipantResponseDto;
import com.toyproject.project.domain.participant.service.ParticipantService;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.login.AuthenticationMember;
import com.toyproject.project.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
@Slf4j
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/{boardId}")
    public ResponseEntity<ApiResponse<String>> applyParticipant(
            @PathVariable Long boardId,
            @AuthenticationMember Member member) {
        participantService.applyParticipant(boardId, member);
        return ApiResponse.success(null, "참가 신청이 완료되었습니다.");
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<List<ParticipantResponseDto>>> getParticipant(
            @PathVariable Long boardId,
            @AuthenticationMember Member member) {
        return ApiResponse.success(participantService.getParticipant(boardId, member), "신청자 조회 성공");
    }

}
