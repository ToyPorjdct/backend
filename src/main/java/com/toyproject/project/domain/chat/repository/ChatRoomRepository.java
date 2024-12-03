package com.toyproject.project.domain.chat.repository;

import com.toyproject.project.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
