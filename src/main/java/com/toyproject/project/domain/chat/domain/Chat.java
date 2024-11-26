package com.toyproject.project.domain.chat.domain;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat")
public class Chat {

    @Id
    @Column(name = "chat_id")
    private String id;
    private String roomId;
    private String message;
    private Long sender;
    private Long receiver;

    @CreatedDate
    private LocalDateTime createdAt;

}
