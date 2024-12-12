package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Likes;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.board.repository.LikesRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.toyproject.project.global.exception.ErrorCode.ALREADY_EXIST_MEMBER;
import static com.toyproject.project.global.exception.ErrorCode.DUPLICATE_LIKE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @InjectMocks
    private LikesService likesService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private Board Board;

    private Member savedMember1;
    private Board savedBoard;
    private Board board1;
    private Board board2;

    @BeforeEach
    public void setUp() {
        // Member 1 초기화
        savedMember1 = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .build();

        savedBoard = Board.builder()
                .id(1L)
                .title("글제목")
                .description("내용")
                .maxParticipant(5)
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .member(savedMember1)
                .build();

        board1 = Board.builder()
                .id(1L)
                .title("Test Board 1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .destination("Seoul")
                .maxParticipant(5)
                .isClosed(false)
                .viewCount(100)
                .likesCount(10)
                .member(savedMember1)
                .build();

        board2 = Board.builder()
                .id(2L)
                .title("Test Board 2")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .destination("Busan")
                .maxParticipant(8)
                .isClosed(true)
                .viewCount(200)
                .likesCount(20)
                .member(savedMember1)
                .build();
    }

    @Test
    @DisplayName("좋아요 추가")
    void addLike() {
        // given
        Board mockBoard = mock(Board.class);
        when(boardRepository.findById(any(Long.class))).thenReturn(Optional.of(mockBoard));
        when(likesRepository.existsByBoardAndMember(any(Board.class), any(Member.class))).thenReturn(false);
        when(likesRepository.save(any(Likes.class))).thenReturn(Likes.builder().board(mockBoard).member(savedMember1).build());

        // when
        likesService.addLike(savedMember1, 1L);

        // then
        verify(boardRepository, times(1)).findById(any(Long.class));
        verify(likesRepository, times(1)).existsByBoardAndMember(any(Board.class), any(Member.class));
        verify(likesRepository, times(1)).save(any(Likes.class));
        verify(mockBoard, times(1)).updateLikesCount(anyInt());
    }

    @Test
    @DisplayName("좋아요 추가 실패: 이미 좋아요 한 게시글")
    void addLikeFail() {
        // given
        Board mockBoard = mock(Board.class);
        when(boardRepository.findById(any(Long.class))).thenReturn(Optional.of(mockBoard));
        when(likesRepository.existsByBoardAndMember(any(Board.class), any(Member.class))).thenReturn(true);

        // when
        assertThatThrownBy(() ->  likesService.addLike(savedMember1, 1L))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("ErrorCode", DUPLICATE_LIKE);

        // then
        verify(boardRepository, times(1)).findById(any(Long.class));
        verify(likesRepository, times(1)).existsByBoardAndMember(any(Board.class), any(Member.class));
        verify(likesRepository, never()).save(any(Likes.class));
        verify(mockBoard, never()).updateLikesCount(anyInt());
    }

    @Test
    @DisplayName("좋아요 취소")
    void removeLike() {
        // given
        Board mockBoard = mock(Board.class);
        when(boardRepository.findById(any(Long.class))).thenReturn(Optional.of(mockBoard));
        when(likesRepository.findByBoardAndMember(any(Board.class), any(Member.class))).thenReturn(Optional.of(Likes.builder().board(mockBoard).member(savedMember1).build()));

        // when
        likesService.removeLike(savedMember1, 1L);

        // then
        verify(boardRepository, times(1)).findById(any(Long.class));
        verify(likesRepository, times(1)).findByBoardAndMember(any(Board.class), any(Member.class));
        verify(likesRepository, times(1)).delete(any(Likes.class));
        verify(mockBoard, times(1)).updateLikesCount(anyInt());
    }
}