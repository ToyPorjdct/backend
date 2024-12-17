package com.toyproject.project.domain.chat.service;


import com.toyproject.project.domain.chat.domain.Chat;
import com.toyproject.project.domain.chat.dto.ChatMessage;
import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.repository.ChatRoomRepository;
import com.toyproject.project.domain.chat.repository.MemberChatRoomRepository;
import com.toyproject.project.domain.chat.repository.mongo.ChatRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberChatRoomRepository memberChatRoomRepository;

    private Member savedMember1, savedMember2;

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

    }

    @Test
    @DisplayName("채팅방 생성")
    void createChatRoom() {
        // given
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest("1:1 Chat", 2L);
        when(memberRepository.findById(any())).thenReturn(java.util.Optional.of(savedMember2));

        // when
        chatService.createChatRoom(savedMember1, chatRoomRequest);

        // then
        verify(memberRepository, times(1)).findById(any());
        verify(chatRoomRepository, times(1)).save(any());
        verify(memberChatRoomRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("채팅방 이전 메세지 조회")
    void getChatListByRoom() {
        // given

        // when
        chatService.getChatListByRoom(1L);

        // then
        verify(chatRepository, times(1)).findByRoomIdOrderByCreatedAtAsc(1L);
    }

    @Test
    @DisplayName("채팅 메시지 저장")
    void saveChat() {
        // given
        ChatMessage chatMessage = new ChatMessage("Hello");

        Chat chat = Chat.builder()
                .message(chatMessage.getMessage())
                .roomId(1L)
                .sender(savedMember1.getId())
                .build();

        // when
        when(jwtTokenProvider.getMemberId("JwtToken")).thenReturn("1");
        when(memberRepository.findById(1L)).thenReturn(java.util.Optional.of(savedMember1));
        when(chatRepository.save(any())).thenReturn(chat);

        chatService.saveChatMessage( chatMessage,1L, "JwtToken");


        // then
        verify(jwtTokenProvider, times(1)).getMemberId("JwtToken");
        verify(chatRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("채팅방 목록 조회")
    void getChatRoomList() {
        // given

        // when
        chatService.getChatRoomList(savedMember1);

        // then
        verify(memberChatRoomRepository, times(1)).findByMemberId(savedMember1.getId());
    }




}