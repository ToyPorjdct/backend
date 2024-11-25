package com.toyproject.project.domain.chat.repository;

import com.toyproject.project.domain.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String>{
    List<Chat> findByRoomId(String roomId);
}
