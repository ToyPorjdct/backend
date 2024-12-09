package com.toyproject.project.domain.chat.repository;

import com.toyproject.project.domain.chat.domain.ChatRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private ChatRoom chatRoom;

    @BeforeEach
    public void setUp() {
        chatRoom = ChatRoom.builder()
                .name("ChatRoom1")
                .build();
    }

    @Test
    @DisplayName("save: 채팅방 저장")
    public void save() {
        // given

        // when
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // then
        verifyChatRoomEquals(chatRoom, savedChatRoom);
    }

    private void verifyChatRoomEquals(ChatRoom expected, ChatRoom actual) {
        assertEquals(actual.getName(), expected.getName());
    }

}