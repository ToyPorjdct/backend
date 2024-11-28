package com.toyproject.project.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomListResponse {
    private String name;
    private String id;
}
