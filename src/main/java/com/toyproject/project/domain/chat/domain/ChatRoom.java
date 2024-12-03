package com.toyproject.project.domain.chat.domain;


import com.toyproject.project.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;




@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String name;
}
