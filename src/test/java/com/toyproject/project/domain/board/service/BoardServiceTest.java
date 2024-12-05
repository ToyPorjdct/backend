package com.toyproject.project.domain.board.service;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.domain.Tag;
import com.toyproject.project.domain.board.domain.TagList;
import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.repository.BoardRepository;
import com.toyproject.project.domain.board.repository.TagListRepository;
import com.toyproject.project.domain.board.repository.TagRepository;
import com.toyproject.project.domain.matching.domain.Matching;
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

import java.time.LocalDate;
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

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagListRepository tagListRepository;

    private Member savedMember1;
    private Member savedMember2;
    private Board savedBoard;
    private Matching savedMatching;

    @BeforeEach
    public void setUp() {
        // Member 1 초기화
        savedMember1 = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .build();

        // Member 2 초기화
        savedMember2 = Member.builder()
                .id(2L)
                .email("test2@gmail.com")
                .nickname("test2")
                .password("1234")
                .build();

        // Board 초기화
        savedBoard = Board.builder()
                .id(1L)
                .title("글제목")
                .description("내용")
                .maxParticipant(5)
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .member(savedMember1)
                .build();

        // 태그 초기화
        Tag tag1 = Tag.builder()
                .id(1L)
                .name("태그1")
                .build();

        Tag tag2 = Tag.builder()
                .id(2L)
                .name("태그2")
                .build();

        // TagList 초기화 (태그와 게시글의 연결 테이블)
        TagList tagList1 = TagList.builder()
                .id(1L)
                .board(savedBoard)
                .tag(tag1)
                .build();

        TagList tagList2 = TagList.builder()
                .id(2L)
                .board(savedBoard)
                .tag(tag2)
                .build();

        // Matching 초기화
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
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tagListRepository.save(any(TagList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        boardService.createBoard(
                BoardCreateRequestDto.builder()
                        .title("글제목")
                        .description("내용")
                        .destination("몽골")
                        .maxParticipant(5)
                        .startDate(LocalDate.parse("2024-11-21"))
                        .endDate(LocalDate.parse("2024-11-25"))
                        .tagNames(List.of("태그1", "태그2"))
                        .build(),
                savedMember1
        );

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
        verify(tagRepository, times(2)).findByName(anyString());
        verify(tagRepository, times(2)).save(any(Tag.class));
        verify(tagListRepository, times(2)).save(any(TagList.class));
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

        when(boardRepository.findByIdWithMember(boardId)).thenReturn(Optional.of(savedBoard));
        when(tagListRepository.findByWithTag(savedBoard)).thenReturn(List.of());

        // when
        boardService.getBoardDetail(boardId);

        // then
        verify(boardRepository).findByIdWithMember(boardId);
        verify(tagListRepository).findByWithTag(savedBoard);
    }

    @Test
    @DisplayName("모집글 간단 조회 성공")
    void getBoardList() {
        // given
        when(boardRepository.findAllWithMember()).thenReturn(List.of(savedBoard));

        // when
        boardService.getBoardList();

        // then
        verify(boardRepository).findAllWithMember();
    }


    @Test
    @DisplayName("동행 신청자 조회 성공")
    void getMatchingList() {
        // given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
        when(matchingRepository.findByBoardIdAndStatus(boardId, PENDING)).thenReturn(List.of(savedMatching));

        // when
        boardService.getMatchingList(boardId, savedMember1);

        // then
        verify(boardRepository).findById(boardId);
        verify(matchingRepository).findByBoardIdAndStatus(boardId, PENDING);
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
        boardService.getCurrentParticipant(boardId);

        // then
        verify(matchingRepository).findByBoardIdAndStatus(boardId, APPROVED);
    }


}