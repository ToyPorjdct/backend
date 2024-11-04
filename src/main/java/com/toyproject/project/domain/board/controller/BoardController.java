package com.toyproject.project.domain.board.controller;

import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
import com.toyproject.project.domain.board.service.BoardService;
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
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBoard(
            @RequestBody BoardCreateRequestDto boardCreateRequestDto,
            @AuthenticationMember Member member) {
        boardService.createBoard(boardCreateRequestDto, member);
        return ApiResponse.success(null, "모집글 작성 성공");
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailResponseDto>> getDetailBoards(
            @PathVariable Long boardId
    ) {
        return ApiResponse.success(boardService.getDetailBoards(boardId), "모집글 상세 조회 성공");
    }


}
