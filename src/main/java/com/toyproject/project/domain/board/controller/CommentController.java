package com.toyproject.project.domain.board.controller;

import com.toyproject.project.domain.board.dto.CommentAddRequestDto;
import com.toyproject.project.domain.board.dto.CommentListResponseDto;
import com.toyproject.project.domain.board.service.CommentService;
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
@Tag(name = "Comment", description = "댓글 API")
@RequestMapping("/board")
public class CommentController {

    private final CommentService commentService;

     @PostMapping("/{boardId}/comment")
     @Operation(summary = "댓글 작성")
     public ResponseEntity<ApiResponse<String>> addComment(
             @AuthenticationMember Member member,
             @PathVariable Long boardId,
             @RequestBody CommentAddRequestDto commentAddRequestDto) {
         commentService.addComment(commentAddRequestDto, member, boardId);
         return ApiResponse.success(null, "success");
     }

    @GetMapping("/{boardId}/comment")
    @Operation(summary = "댓글 조회")
    public ResponseEntity<ApiResponse<List<CommentListResponseDto>>> commentList(@PathVariable Long boardId) {
        return ApiResponse.success(commentService.getCommentList(boardId), "success");
    }


}
