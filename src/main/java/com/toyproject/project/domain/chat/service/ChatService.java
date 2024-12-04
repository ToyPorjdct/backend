package com.toyproject.project.domain.chat.service;


import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import com.toyproject.project.domain.chat.domain.Chat;
import com.toyproject.project.domain.chat.domain.ChatRoom;
import com.toyproject.project.domain.chat.domain.MemberChatRoom;
import com.toyproject.project.domain.chat.dto.ChatMessage;
import com.toyproject.project.domain.chat.dto.ChatResponse;
import com.toyproject.project.domain.chat.dto.ChatRoomListResponse;
import com.toyproject.project.domain.chat.dto.ChatRoomRequest;
import com.toyproject.project.domain.chat.repository.MemberChatRoomRepository;
import com.toyproject.project.domain.chat.repository.mongo.ChatRepository;
import com.toyproject.project.domain.chat.repository.ChatRoomRepository;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;



    /**
     * 채팅방 생성
     */
    public void createChatRoom(Member cuurentMember, ChatRoomRequest chatRoomRequest) {
        Member otherMember = memberRepository.findById(chatRoomRequest.getOtherMemberId()).orElseThrow();

        chatRoomRepository.save(
                ChatRoom.builder()
                .name(chatRoomRequest.getName())
                .build()
        );

        memberChatRoomRepository.save(
                MemberChatRoom.builder()
                .member(cuurentMember)
                .chatRoom(
                        ChatRoom.builder()
                        .name(chatRoomRequest.getName())
                        .build()
                )
                .build()
        );

        memberChatRoomRepository.save(
                MemberChatRoom.builder()
                .member(otherMember)
                .chatRoom(ChatRoom.builder()
                        .name(chatRoomRequest.getName())
                        .build())
                .build()
        );
    }

    /**
     * 채티방 이전 메세지 조회
     */
    public List<ChatResponse> getChatListByRoom(Long roomId) {
        List<Chat> chatList = chatRepository.findByRoomIdOrderByCreatedAtAsc(roomId);

        return chatList.stream()
                .map(chat -> {
                    AuthorResponseDto author = memberRepository.findById(chat.getSender())
                            .map(member -> new AuthorResponseDto(
                                    member.getId(),
                                    member.getNickname(),
                                    member.getProfileImage()))
                            .orElseThrow();

                    return ChatResponse.builder()
                            .chatId(chat.getId())
                            .message(chat.getMessage())
                            .author(author)
                            .createdAt(chat.getCreatedAt().toString())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 채팅 메세지 저장
     */
    public Chat saveChatMessage(ChatMessage chatMessage, Long roomId, String token) {
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
    public List<ChatRoomListResponse> getChatRoomList(Member cuurentMember) {
        List<MemberChatRoom> memberChatRoomList = memberChatRoomRepository.findByMemberId(cuurentMember.getId());

        return memberChatRoomList.stream()
                .map(memberChatRoom -> {
                    ChatRoom chatRoom = memberChatRoom.getChatRoom();
                    return new ChatRoomListResponse(chatRoom.getId(), chatRoom.getName());
                })
                .collect(Collectors.toList());
    }

}

