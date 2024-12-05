package com.toyproject.project.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toyproject.project.domain.board.domain.Board;
import com.toyproject.project.domain.board.dto.BoardCreateRequestDto;
import com.toyproject.project.domain.board.dto.BoardDetailResponseDto;
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
                .startDate(LocalDate.parse("2024-11-21T13:47:13.248"))
                .endDate(LocalDate.parse("2024-11-25T13:47:13.248"))
                .member(savedMember1)
                .build();
    }

    @Test
    @DisplayName("모집 생성 API")
    void createBoard() throws Exception {
        //given: mock 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
        mapper.registerModule(new JavaTimeModule());

        BoardCreateRequestDto boardCreateRequestDto = BoardCreateRequestDto.builder()
                .title("title")
                .description("direction")
                .maxParticipant(10)
                .startDate(LocalDate.parse("2024-11-21T13:47:13.248"))
                .endDate(LocalDate.parse("2024-11-25T13:47:13.248"))
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
                .title("Sample Title")
                .description("Sample Description")
                .maxParticipant(10)
                .startDate(LocalDate.parse("2024-11-21T13:47:13"))
                .endDate(LocalDate.parse("2024-11-25T13:47:13"))
                .build();

        given(boardService.getBoardDetail(1L)).willReturn(boardDetailDto);

        //when
        mockMvc.perform(get("/board/{boardId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.title").value("Sample Title"))
                .andExpect(jsonPath("$.result.description").value("Sample Description"))
                .andExpect(jsonPath("$.result.maxParticipant").value(10));


        //then
        verify(boardService).getBoardDetail(1L);
    }




}