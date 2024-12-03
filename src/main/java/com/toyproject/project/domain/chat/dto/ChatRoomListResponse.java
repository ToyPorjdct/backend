package com.toyproject.project.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomListResponse {
    private Long roomId;
    private String name;
}
