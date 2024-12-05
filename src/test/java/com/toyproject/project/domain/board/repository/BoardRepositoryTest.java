package com.toyproject.project.domain.board.repository;

import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member savedMember1;
    private Board board;

    @BeforeEach
    public void setUp() {
        savedMember1 = Member.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .profileImage("test.jpg")
                .build();

        savedMember1 = memberRepository.save(savedMember1);

        board = Board.builder()
                .title("Test Board")
                .description("Test Description")
                .destination("Test Destination")
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .maxParticipant(5)
                .member(savedMember1)
                .isClosed(false)
                .build();
    }

    @Test
    @DisplayName("save")
    public void save() {
        // given
        // when
        Board savedBoard = boardRepository.save(board);

        verifyBoardEquals(board, savedBoard);
    }

    @Test
    @DisplayName("findById")
    public void findById() {
        // given
        Board savedBoard = boardRepository.save(board);

        // when
        Board findBoard = boardRepository.findById(savedBoard.getId()).get();

        // then
        verifyBoardEquals(findBoard, savedBoard);
    }


    @Test
    @DisplayName("delete")
    public void delete() {
        // given
        Board savedBoard = boardRepository.save(board);

        // when
        boardRepository.delete(savedBoard);

        // then
        assertThat(boardRepository.findById(savedBoard.getId())).isEmpty();
    }

    @Test
    @DisplayName("findByIdWithMember")
    public void findByIdWithMember() {
        // given
        Board savedBoard = boardRepository.save(board);

        // when
        Board findBoard = boardRepository.findByIdWithMember(savedBoard.getId()).get();

        // then
        verifyBoardEquals(findBoard, savedBoard);
    }


    @Test
    @DisplayName("findAllWithMember")
    public void findAllWithMember() {
        // given
        Board savedBoard = boardRepository.save(board);

        // when
        Board findBoard = boardRepository.findAllWithMember().get(0);

        // then
        verifyBoardEquals(findBoard, savedBoard);
    }


    private static void verifyBoardEquals(Board actual, Board expected) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getDestination()).isEqualTo(expected.getDestination());
        assertThat(actual.getStartDate()).isEqualTo(expected.getStartDate());
        assertThat(actual.getEndDate()).isEqualTo(expected.getEndDate());
        assertThat(actual.getMaxParticipant()).isEqualTo(expected.getMaxParticipant());
        assertThat(actual.getViewCount()).isEqualTo(expected.getViewCount());
        assertThat(actual.getLikesCount()).isEqualTo(expected.getLikesCount());
        assertThat(actual.isClosed()).isEqualTo(expected.isClosed());
        assertThat(actual.getMember()).isNotNull();
    }

}