package com.toyproject.project.domain.chat.service;


import com.toyproject.project.domain.chat.domain.Chat;
import com.toyproject.project.domain.chat.domain.ChatRoom;
import com.toyproject.project.domain.chat.dto.ChatMessage;
import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.repository.ChatRepository;
import com.toyproject.project.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;


    public ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    /**
     * 채팅방 생성
     */
    public ChatRoom createChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomRequest.getName())
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
    public void saveChatMessage(ChatMessage chatMessage) {
        Chat chat = Chat.builder()
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .receiver(chatMessage.getReceiver())
                .message(chatMessage.getMessage())
                .build();
        chatRepository.save(chat);
    }
}

