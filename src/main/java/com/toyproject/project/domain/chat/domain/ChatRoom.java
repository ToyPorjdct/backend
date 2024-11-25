package com.toyproject.project.domain.chat.domain;


import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



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

}
