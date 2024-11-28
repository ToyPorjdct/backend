package com.toyproject.project.domain.chat.contorller;

import com.toyproject.project.domain.chat.dto.ChatRoomListResponse;
import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.service.ChatService;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.login.AuthenticationMember;
import com.toyproject.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "채팅 API")
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "채팅방 생성")
    public String createChatRoom(
            @AuthenticationMember Member member,
            @RequestBody ChatRoomRequest chatRoomRequest) {
        chatService.createChatRoom(member, chatRoomRequest);
        return "success";
    }

    @GetMapping
    @Operation(summary = "채팅방 목록 조회")
    public ResponseEntity<ApiResponse<List<ChatRoomListResponse>>> getChatRoom(
            @AuthenticationMember Member member) {
        return ApiResponse.success(chatService.getChatRoomList(member), "success");

    }
}
