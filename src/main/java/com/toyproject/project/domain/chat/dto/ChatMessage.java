package com.toyproject.project.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String roomId;
    private Long sender;
    private Long receiver;
    private String message;
}
