package com.toyproject.project.domain.board.controller;

import com.toyproject.project.domain.board.dto.BoardListResponseDto;
import com.toyproject.project.domain.board.service.LikesService;
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
@Tag(name = "Likes", description = "좋아요 API")
public class LikeController {

    private final LikesService likesService;

    @PostMapping("/board/{boardId}/likes")
    @Operation(summary = "좋아요 추가", description = "좋아요 추가")
    public ResponseEntity<ApiResponse<String>> likeAdd(@AuthenticationMember Member member, @PathVariable Long boardId) {
        likesService.addLike(member, boardId);
        return ApiResponse.success(null, "success");
    }

    @DeleteMapping("/board/{boardId}/likes")
    @Operation(summary = "좋아요 삭제", description = "좋아요 삭제")
    public ResponseEntity<ApiResponse<String>> likeRemove(@AuthenticationMember Member member, @PathVariable Long boardId) {
        likesService.removeLike(member, boardId);
        return ApiResponse.success(null, "success");
    }

    @GetMapping("/member/likes")
    @Operation(summary = "좋아요 목록 조회", description = "좋아요 목록 조회")
    public ResponseEntity<ApiResponse<List<BoardListResponseDto>>> likeList(@AuthenticationMember Member member) {
        return ApiResponse.success(likesService.getLikeBoardList(member), "success");
    }
}
