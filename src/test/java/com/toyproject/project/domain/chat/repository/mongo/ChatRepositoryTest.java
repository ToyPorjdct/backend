package com.toyproject.project.domain.chat.repository.mongo;

import com.toyproject.project.domain.chat.domain.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@DataMongoTest
class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;


    private Chat chat;

    @BeforeEach
    public void setUp() {
        chat = Chat.builder()
                .message("Hello")
                .roomId(1L)
                .sender(1L)
                .build();
    }

    @Test
    @DisplayName("save: 채팅 메세지 저장")
    public void save() {
        // given

        // when
        Chat savedChat = chatRepository.save(chat);

        // then
        verifyChatEquals(chat, savedChat);
    }

    @Test
    @DisplayName("findByRoomIdOrderByCreatedAtAsc: 이전채팅 조회")
    public void findByRoomIdOrderByCreatedAtAsc() {
        // given
        chatRepository.save(chat);

        // when
        Chat findChat = chatRepository.findByRoomIdOrderByCreatedAtAsc(1L).get(0);

        // then
        verifyChatEquals(chat, findChat);
    }

    private void verifyChatEquals(Chat expected, Chat actual) {
        assertEquals(actual.getMessage(), expected.getMessage());
        assertEquals(actual.getRoomId(), expected.getRoomId());
        assertEquals(actual.getSender(), expected.getSender());
    }


}