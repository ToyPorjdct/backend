package com.toyproject.project.domain.chat.service;


import com.toyproject.project.domain.chat.domain.Chat;
import com.toyproject.project.domain.chat.domain.ChatRoom;
import com.toyproject.project.domain.chat.dto.ChatMessage;
import com.toyproject.project.domain.chat.dto.ChatRoomListResponse;
import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.repository.ChatRepository;
import com.toyproject.project.domain.chat.repository.ChatRoomRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.global.exception.CustomException;
import com.toyproject.project.global.exception.ErrorCode;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    /**
     * 채팅방 생성
     */
    public ChatRoom createChatRoom(Member member, ChatRoomRequest chatRoomRequest) {
        if(chatRoomRequest.getMember() == null || chatRoomRequest.getMember() == member.getId()){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomRequest.getName())
                .memberlist(new HashSet<>(Arrays.asList(member.getId(), chatRoomRequest.getMember())))
                .build();
        return chatRoomRepository.save(chatRoom);
    }


    /**
     * 채팅방의 모든 메세지 조회
     */
    public List<Chat> getChatListByRoom(String roomId) {
        return chatRepository.findByRoomId(roomId);
    }

    /**
     * 채팅 메세지 저장
     */
    public Chat saveChatMessage(ChatMessage chatMessage, String roomId, String token) {
        Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(token));
        return chatRepository.save(
                Chat.builder()
                .roomId(roomId)
                .sender(memberId)
                .message(chatMessage.getMessage())
                .build()
        );
    }

    /**
     * 채팅방 목록 조회
     */
    public List<ChatRoomListResponse> getChatRoomList(Member member) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByMemberlistContaining(member.getId());
        return chatRoomList.stream()
                .map(chatRoom -> new ChatRoomListResponse(chatRoom.getName(), chatRoom.getId()))
                .collect(Collectors.toList());
    }
}

