package com.toyproject.project.global.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.project.domain.chat.domain.Chat;
import com.toyproject.project.domain.chat.dto.ChatMessage;
import com.toyproject.project.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ObjectMapper mapper;
    private final ChatService chatService;


    /**
     * WebSocket 연결시 호출되는 메서드
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("세션 연결 sessionId: {}", session.getId());
        sessions.add(session);

        String roomId = session.getUri().getQuery();
        log.info("roomId: {}", roomId);

        chatService.getChatRoom(roomId);
        List<Chat> previousChats = chatService.getChatListByRoom(roomId);

        for (Chat chat : previousChats) {
            log.info("Previous chat: {}", chat.getMessage());
            session.sendMessage(new TextMessage(mapper.writeValueAsString(chat)));
        }
    }

    /**
     * 메시지를 받았을 때 호출되는 메서드
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message: {}", message.getPayload());

        ChatMessage chatMessage = mapper.readValue(message.getPayload(), ChatMessage.class);

        chatService.saveChatMessage(chatMessage);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message.getPayload()));
            }
        }
    }

    /**
     * WebSocket 연결이 끊어졌을 때 호출되는 메서드
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("세션 연결 끊김 sessionId: {}", session.getId());
        sessions.remove(session);
    }
}

