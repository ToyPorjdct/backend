package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.matching.domain.Matching;
import com.toyproject.project.domain.matching.dto.MatchingResponseDto;
import com.toyproject.project.domain.matching.repository.MatchingRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.APPROVED;
import static com.toyproject.project.domain.matching.domain.status.MatchingStatus.PENDING;
import static com.toyproject.project.global.exception.ErrorCode.NO_AUTHORITY;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MatchingRepository matchingRepository;

    private Member savedMember1;
    private Member savedMember2;
    private Board savedBoard;
    private Matching savedMatching;

    @BeforeEach
    public void setUp() {
        savedMember1 = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .build();

        savedMember2 = Member.builder()
                .id(2L)
                .email("test2@gmail.com")
                .nickname("test2")
                .password("1234")
                .build();

        savedBoard = Board.builder()
                .id(1L)
                .title("글제목")
                .description("내용")
                .maxParticipant(5)
                .startDate(LocalDateTime.parse("2024-11-21T13:47:13.248"))
                .endDate(LocalDateTime.parse("2024-11-25T13:47:13.248"))
                .member(savedMember1)
                .build();

        savedMatching = Matching.builder()
                .id(1L)
                .board(savedBoard)
                .member(savedMember2)
                .build();
    }

    @Test
    @DisplayName("모집글 작성 성공")
    void createBoard() {
        // given
        // when
        boardService.createBoard(
                BoardCreateRequestDto.builder()
                        .title("글제목")
                        .description("내용")
                        .maxParticipant(5)
                        .startDate(LocalDateTime.parse("2024-11-21T13:47:13.248"))
                        .endDate(LocalDateTime.parse("2024-11-25T13:47:13.248"))
                        .build(),
                savedMember1
        );

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("모집글 삭제 성공")
    void removeBoard() {
        // given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));

        // when
        boardService.removeBoard(boardId, savedMember1);

        // then
        verify(matchingRepository).deleteByBoardId(boardId);
        verify(boardRepository).delete(savedBoard);
    }

    @Test
    @DisplayName("모집글 삭제 실패: 권한 없음")
    void removeBoardFail() {
        // given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));

        // when
        assertThatThrownBy(() -> boardService.removeBoard(boardId, savedMember2))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("ErrorCode", NO_AUTHORITY);

        // then
        verify(matchingRepository, never()).deleteByBoardId(anyLong());
        verify(boardRepository, never()).delete(any(Board.class));
    }

    @Test
    @DisplayName("모집글 상세 조회 성공")
    void getDetailBoards() {
        // given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));

        // when
        BoardDetailResponseDto response = boardService.getDetailBoards(boardId);

        // then
        verify(boardRepository).findById(boardId);
        assertThat(response.getId()).isEqualTo(savedBoard.getId());
        assertThat(response.getTitle()).isEqualTo(savedBoard.getTitle());
        assertThat(response.getDescription()).isEqualTo(savedBoard.getDescription());
        assertThat(response.getMemberNickname()).isEqualTo(savedBoard.getMember().getNickname());
        assertThat(response.getMaxParticipant()).isEqualTo(savedBoard.getMaxParticipant());
        assertThat(response.getCurrentParticipant()).isEqualTo(savedBoard.getCurrentParticipant());
        assertThat(response.getStartDate()).isEqualTo(savedBoard.getStartDate());
        assertThat(response.getEndDate()).isEqualTo(savedBoard.getEndDate());
    }

    @Test
    @DisplayName("동행 신청자 조회 성공")
    void getMatchingList() {
        // given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
        when(matchingRepository.findByBoardIdAndStatus(boardId, PENDING)).thenReturn(List.of(savedMatching));

        // when
        List<MatchingResponseDto> matchingList = boardService.getMatchingList(boardId, savedMember1);

        // then
        verify(boardRepository).findById(boardId);

        assertThat(matchingList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("동행 신청자 조회 실패: 권한 없음")
    void getMatchingListFail() {
        // given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));

        // when
        assertThatThrownBy(() -> boardService.getMatchingList(boardId, savedMember2))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("ErrorCode", NO_AUTHORITY);

        // then
        verify(matchingRepository, never()).findByBoardIdAndStatus(anyLong(), any());
    }

    @Test
    @DisplayName("현재 참가자 조회")
    void getCurrentParticipant() {
        // given
        Long boardId = 1L;

        when(matchingRepository.findByBoardIdAndStatus(boardId, APPROVED)).thenReturn(List.of(savedMatching));

        // when
        List<MatchingResponseDto> currentParticipant = boardService.getCurrentParticipant(boardId);

        // then
        verify(matchingRepository).findByBoardIdAndStatus(boardId, APPROVED);
        assertThat(currentParticipant.size()).isEqualTo(1);
    }


}