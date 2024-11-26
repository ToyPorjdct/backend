package com.toyproject.project.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.project.domain.chat.domain.Chat;
import com.toyproject.project.domain.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final ChatService chatService;

    private final ConcurrentHashMap<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("세션 연결 sessionId: {}", session.getId());

        String roomId = session.getUri().getQuery();
        chatService.getChatRoom(roomId);

        // roomId에 해당하는 세션 저장
        roomSessions.computeIfAbsent(roomId, key -> new CopyOnWriteArraySet<>()).add(session);

        // 이전 채팅 내역 전송
        List<Chat> previousChats = chatService.getChatListByRoom(roomId);
        for (Chat chat : previousChats) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(chat)));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message: {}", message.getPayload());

        ChatMessage chatMessage = mapper.readValue(message.getPayload(), ChatMessage.class);
        String roomId = chatMessage.getRoomId();

        if (roomId == null || roomId.isEmpty()) {
            log.error("roomId is missing in the message");
            return;
        }

        chatService.saveChatMessage(chatMessage);

        // 해당 roomId에 연결된 세션들에게만 메시지 전송
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("세션 해제 sessionId: {}", session.getId());

        // 모든 roomId에 대해 세션 제거
        roomSessions.forEach((roomId, sessions) -> {
            if (sessions.remove(session)) {
                log.info("Session removed from roomId: {}", roomId);
            }
        });
    }
}


