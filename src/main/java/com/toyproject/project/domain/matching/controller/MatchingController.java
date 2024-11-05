package com.toyproject.project.domain.matching.controller;


import com.toyproject.project.domain.matching.dto.MatchingResponseDto;
import com.toyproject.project.domain.matching.service.MatchingService;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.login.AuthenticationMember;
import com.toyproject.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
@Tag(name = "Matching", description = "매칭 API")
@Slf4j
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<String>> matchingCreate(
            @PathVariable Long boardId,
            @AuthenticationMember Member member) {
        matchingService.createMatching(boardId, member);
        return ApiResponse.success(null, "참가 신청이 완료되었습니다.");
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<List<MatchingResponseDto>>> matchingList(
            @PathVariable Long boardId,
            @AuthenticationMember Member member) {
        return ApiResponse.success(matchingService.getMatchingList(boardId, member), "신청자 조회 성공");
    }

    @PutMapping("/{matchingId}/accept")
    public ResponseEntity<ApiResponse<String>> matchingAccept(
            @PathVariable Long matchingId,
            @AuthenticationMember Member member) {
        matchingService.acceptMatching(matchingId, member);
        return ApiResponse.success(null, "참가 신청을 수락하였습니다.");
    }


}
