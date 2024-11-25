package com.toyproject.project.domain.chat.contorller;

import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "채팅 API")
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "채팅방 생성")
    public String createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        chatService.createChatRoom(chatRoomRequest);
        return "success";
    }
}
