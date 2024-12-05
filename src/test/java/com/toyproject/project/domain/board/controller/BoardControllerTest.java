package com.toyproject.project.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
import com.toyproject.project.domain.board.dto.BoardListResponseDto;
import com.toyproject.project.domain.board.service.BoardService;
import com.toyproject.project.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    private ObjectMapper mapper = new ObjectMapper();
    private Member savedMember1;
    private Board savedBoard1;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();

        savedMember1 = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .build();

        savedBoard1 = Board.builder()
                .id(1L)
                .title("title")
                .description("direction")
                .maxParticipant(10)
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .member(savedMember1)
                .build();
    }

    @Test
    @DisplayName("모집글 작성 API")
    void createBoard() throws Exception {
        //given: mock 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
        mapper.registerModule(new JavaTimeModule());

        BoardCreateRequestDto boardCreateRequestDto = BoardCreateRequestDto.builder()
                .title("title")
                .description("direction")
                .destination("여행지")
                .maxParticipant(5)
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .tagNames(List.of("tag1", "tag2"))
                .build();

        String json = mapper.writeValueAsString(boardCreateRequestDto);

        // andExpect: 기대하는 값이 나왔는지 체크
        mockMvc.perform(post("/board")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // verify: 해당 객체의 메소드가 실행되었는지 체크
        verify(boardService).createBoard(any(BoardCreateRequestDto.class), any(Member.class));
    }

    @Test
    @DisplayName("모집글 삭제 API")
    void removeBoard() throws Exception {
        //given

        //when
        mockMvc.perform(delete("/board/{boardId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        verify(boardService).removeBoard(any(Long.class), any(Member.class));
    }

    @Test
    @DisplayName("모집글 상세 조회 API")
    void getDetailBoards() throws Exception {
        //given
        BoardDetailResponseDto boardDetailDto = BoardDetailResponseDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .maxParticipant(10)
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .isClosed(false)
                .views(0)
                .likes(0)
                .destination("destination")
                .createdAt(LocalDateTime.now())
                .tags(List.of("tag1", "tag2"))
                .author(new AuthorResponseDto(1L, "test", "test.jpg"))
                .build();

        given(boardService.getBoardDetail(1L)).willReturn(boardDetailDto);

        //when
        mockMvc.perform(get("/board/{boardId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.title").value("title"))
                .andExpect(jsonPath("$.result.description").value("description"))
                .andExpect(jsonPath("$.result.maxParticipant").value(10))
                .andExpect(jsonPath("$.result.startDate").value("11.21"))
                .andExpect(jsonPath("$.result.endDate").value("11.25"))
                .andExpect(jsonPath("$.result.isClosed").value(false))
                .andExpect(jsonPath("$.result.views").value(0))
                .andExpect(jsonPath("$.result.likes").value(0))
                .andExpect(jsonPath("$.result.destination").value("destination"))
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.tags").isArray())
                .andExpect(jsonPath("$.result.author.id").value(1L))
                .andExpect(jsonPath("$.result.author.nickname").value("test"))
                .andExpect(jsonPath("$.result.author.profileImage").value("test.jpg"));

        //then
        verify(boardService).getBoardDetail(1L);
    }

    @Test
    @DisplayName("모집글 전체 조회 API")
    void getBoards() throws Exception {
        //given
        BoardListResponseDto board1 = BoardListResponseDto.builder()
                .id(1L)
                .title("title")
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .destination("destination")
                .maxParticipant(10)
                .isClosed(false)
                .views(0)
                .likes(0)
                .tags(List.of("tag1", "tag2"))
                .author(new AuthorResponseDto(1L, "test", "test.jpg"))
                .build();

        BoardListResponseDto board2 = BoardListResponseDto.builder()
                .id(2L)
                .title("title2")
                .startDate(LocalDate.parse("2024-11-21"))
                .endDate(LocalDate.parse("2024-11-25"))
                .destination("destination2")
                .maxParticipant(10)
                .isClosed(false)
                .views(0)
                .likes(0)
                .tags(List.of("tag1", "tag2"))
                .author(new AuthorResponseDto(1L, "test", "test.jpg"))
                .build();

        given(boardService.getBoardList()).willReturn(List.of(board1, board2));

        //when
        mockMvc.perform(get("/board")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].id").value(1L))
                .andExpect(jsonPath("$.result[0].title").value("title"))
                .andExpect(jsonPath("$.result[0].startDate").value("11.21"))
                .andExpect(jsonPath("$.result[0].endDate").value("11.25"))
                .andExpect(jsonPath("$.result[0].destination").value("destination"))
                .andExpect(jsonPath("$.result[0].maxParticipant").value(10))
                .andExpect(jsonPath("$.result[0].isClosed").value(false))
                .andExpect(jsonPath("$.result[0].views").value(0))
                .andExpect(jsonPath("$.result[0].likes").value(0))
                .andExpect(jsonPath("$.result[0].tags").isArray())
                .andExpect(jsonPath("$.result[0].author.id").value(1L))
                .andExpect(jsonPath("$.result[0].author.nickname").value("test"))
                .andExpect(jsonPath("$.result[0].author.profileImage").value("test.jpg"))

                // 두 번째 게시글(board2) 검증
                .andExpect(jsonPath("$.result[1].id").value(2L))
                .andExpect(jsonPath("$.result[1].title").value("title2"))
                .andExpect(jsonPath("$.result[1].startDate").value("11.21"))
                .andExpect(jsonPath("$.result[1].endDate").value("11.25"))
                .andExpect(jsonPath("$.result[1].destination").value("destination2"))
                .andExpect(jsonPath("$.result[1].maxParticipant").value(10))
                .andExpect(jsonPath("$.result[1].isClosed").value(false))
                .andExpect(jsonPath("$.result[1].views").value(0))
                .andExpect(jsonPath("$.result[1].likes").value(0))
                .andExpect(jsonPath("$.result[1].tags").isArray())
                .andExpect(jsonPath("$.result[1].author.id").value(1L))
                .andExpect(jsonPath("$.result[1].author.nickname").value("test"))
                .andExpect(jsonPath("$.result[1].author.profileImage").value("test.jpg"));

        //then
        verify(boardService).getBoardList();
    }




}