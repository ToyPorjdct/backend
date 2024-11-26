package com.toyproject.project.domain.chat.domain;


import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_room")
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id")
    private String id;
    private String name;

    @Builder.Default
    private Set<Long> memberlist = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;
}
