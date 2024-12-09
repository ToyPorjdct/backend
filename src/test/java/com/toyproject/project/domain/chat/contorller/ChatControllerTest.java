package com.toyproject.project.domain.chat.contorller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import com.toyproject.project.domain.chat.dto.ChatResponse;
import com.toyproject.project.domain.chat.dto.ChatRoomListResponse;
import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.service.ChatService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    private ObjectMapper mapper = new ObjectMapper();

    private Member savedMember1;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();

        savedMember1 = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .build();
    }


    @Test
    @DisplayName("채팅방 생성")
    void createChatRoom() throws Exception {
        // given
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest("ChatRoom1", 1L);

        String json = mapper.writeValueAsString(chatRoomRequest);

        // when
        mockMvc.perform(post("/chat")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(chatService).createChatRoom(any(Member.class), any(ChatRoomRequest.class));
    }

    @Test
    @DisplayName("채팅방 목록 조회")
    void getChatRoom() throws Exception {
        // given
        ChatRoomListResponse response1 = ChatRoomListResponse.builder()
                .roomId(1L)
                .name("ChatRoom1")
                .build();

        ChatRoomListResponse response2 = ChatRoomListResponse.builder()
                .roomId(2L)
                .name("ChatRoom2")
                .build();

        given(chatService.getChatRoomList(any(Member.class))).willReturn(List.of(response1, response2));

        // when
        mockMvc.perform(get("/chat")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].roomId").value(1L))
                .andExpect(jsonPath("$.result[0].name").value("ChatRoom1"))
                .andExpect(jsonPath("$.result[1].roomId").value(2L))
                .andExpect(jsonPath("$.result[1].name").value("ChatRoom2"));

        // then
        verify(chatService).getChatRoomList(any(Member.class));

    }

    @Test
    @DisplayName("채팅방 이전 메세지 조회")
    void testGetChatRoom() throws Exception {
        ChatResponse chatResponse1 = ChatResponse.builder()
                .chatId("aaa-bbb")
                .message("Hello")
                .author(new AuthorResponseDto(savedMember1.getId(), savedMember1.getNickname(), savedMember1.getProfileImage()))
                .build();

        ChatResponse chatResponse2 = ChatResponse.builder()
                .chatId("aaa-ccc")
                .message("Hi")
                .author(new AuthorResponseDto(savedMember1.getId(), savedMember1.getNickname(), savedMember1.getProfileImage()))
                .build();

        given(chatService.getChatListByRoom(1L)).willReturn(List.of(chatResponse1, chatResponse2));

        mockMvc.perform(get("/chat/room/{roomId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].chatId").value("aaa-bbb"))
                .andExpect(jsonPath("$.result[0].message").value("Hello"))
                .andExpect(jsonPath("$.result[0].author.id").value(savedMember1.getId()))
                .andExpect(jsonPath("$.result[0].author.nickname").value(savedMember1.getNickname()))
                .andExpect(jsonPath("$.result[0].author.profileImage").value(savedMember1.getProfileImage()))
                .andExpect(jsonPath("$.result[1].chatId").value("aaa-ccc"))
                .andExpect(jsonPath("$.result[1].message").value("Hi"))
                .andExpect(jsonPath("$.result[1].author.id").value(savedMember1.getId()))
                .andExpect(jsonPath("$.result[1].author.nickname").value(savedMember1.getNickname()))
                .andExpect(jsonPath("$.result[1].author.profileImage").value(savedMember1.getProfileImage()));

        verify(chatService).getChatListByRoom(any(Long.class));
    }
}