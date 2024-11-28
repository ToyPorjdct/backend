package com.toyproject.project.domain.chat.repository;

import com.toyproject.project.domain.chat.domain.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>{
    List<ChatRoom> findByMemberlistContaining(Long id);
}
