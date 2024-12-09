package com.toyproject.project.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomRequest {
    private String name;
    private Long otherMemberId;
}
