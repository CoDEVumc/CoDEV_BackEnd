package com.codevumc.codev_backend.mongo_repository;

import com.codevumc.codev_backend.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    //List<ChatMessage> findByRoomId(String roomId);
    List<ChatMessage> findAll();
    ChatMessage findTopByRoomId(String roomId);
    ChatMessage findTopByRoomIdOrderByCreatedDateDesc(String roomId);
}
