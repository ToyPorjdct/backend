package com.toyproject.project.domain.chat.contorller;

import com.toyproject.project.domain.chat.dto.ChatMessage;
import com.toyproject.project.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class WebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat.{roomId}")  // /pub/chat 주소로 발행된 메시지를
    @SendTo("/sub/chat.{roomId}")      // 이 주소를 구독한 사용자에게 전달
    public String message(
            ChatMessage chatMessage,
            @DestinationVariable Long roomId,
            @Header("Authorization") String token
    ) {
        chatService.saveChatMessage(chatMessage, roomId, token);
        return "success";
    }
}
