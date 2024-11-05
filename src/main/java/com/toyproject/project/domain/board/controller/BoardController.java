package com.toyproject.project.domain.board.controller;

import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
import com.toyproject.project.domain.board.service.BoardService;
import com.toyproject.project.domain.matching.dto.MatchingResponseDto;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.login.AuthenticationMember;
import com.toyproject.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Board", description = "모집글 API")
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "모집글 작성", description = "모집글 작성")
    public ResponseEntity<ApiResponse<String>> boardCreate(
            @RequestBody BoardCreateRequestDto boardCreateRequestDto,
            @AuthenticationMember Member member) {
        boardService.createBoard(boardCreateRequestDto, member);
        return ApiResponse.success(null, "모집글 작성 성공");
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "모집글 삭제", description = "모집글 삭제")
    public ResponseEntity<ApiResponse<String>> boardRemove(
            @PathVariable Long boardId,
            @AuthenticationMember Member member) {
        boardService.removeBoard(boardId, member);
        return ApiResponse.success(null, "모집글 삭제 성공");
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "모집글 상세 조회", description = "모집글 상세 조회")
    public ResponseEntity<ApiResponse<BoardDetailResponseDto>> boardsDetail(
            @PathVariable Long boardId
    ) {
        return ApiResponse.success(boardService.getDetailBoards(boardId), "모집글 상세 조회 성공");
    }

    @GetMapping("/{boardId}/matching")
    @Operation(summary = "매칭 신청자 조회", description = "매칭 신청자 조회")
    public ResponseEntity<ApiResponse<List<MatchingResponseDto>>> matchingList(
            @PathVariable Long boardId,
            @AuthenticationMember Member member) {
        return ApiResponse.success(boardService.getMatchingList(boardId, member), "신청자 조회 성공");
    }

    @GetMapping("/{boardId}/participant")
    @Operation(summary = "현재 참가자 조회", description = "현재 참가자 조회")
    public ResponseEntity<ApiResponse<List<MatchingResponseDto>>> participantList(
            @PathVariable Long boardId
    ) {
        return ApiResponse.success(boardService.getCurrentParticipant(boardId), "참가자 조회 성공");
    }

}
