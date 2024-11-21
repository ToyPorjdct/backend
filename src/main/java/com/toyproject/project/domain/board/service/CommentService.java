package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Comment;
import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import com.toyproject.project.domain.board.dto.CommentAddRequestDto;
import com.toyproject.project.domain.board.dto.CommentListResponseDto;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.board.repository.CommentRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.toyproject.project.global.exception.ErrorCode.NOT_FOUND_BOARD;
import static com.toyproject.project.global.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void addComment(CommentAddRequestDto commentAddRequestDto, Member member, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOARD));

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(commentAddRequestDto.getContent())
                .build();

        commentRepository.save(comment);
    }

    public List<CommentListResponseDto> getCommentList(Long boardId) {
        List<Comment> comments = commentRepository.findByBoarIdWithMember(boardId);

        List<CommentListResponseDto> commentList = comments.stream()
                .map(comment -> CommentListResponseDto.builder()
                        .commentId(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .author(AuthorResponseDto.builder()
                                .nickname(comment.getMember().getNickname())
                                .profileImage(comment.getMember().getProfileImage())
                                .build())
                        .build())
                .collect(Collectors.toList());

        return commentList;
    }

}
