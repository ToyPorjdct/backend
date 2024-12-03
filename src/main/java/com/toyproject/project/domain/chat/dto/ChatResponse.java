package com.toyproject.project.domain.chat.dto;

import com.toyproject.project.domain.board.dto.AuthorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatResponse {
    private String chatId;
    private String message;
    private AuthorResponseDto author;
    private String createdAt;
}
