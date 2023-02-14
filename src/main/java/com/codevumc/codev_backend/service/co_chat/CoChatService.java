package com.codevumc.codev_backend.service.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;

public interface CoChatService {
    CoDevResponse createChatRoom(HttpServletRequest request, ChatRoom chatRoom, String self_email);
    CoDevResponse getChatRooms(String co_email);
    CoDevResponse inviteUser(String roomId, JSONArray co_emails);
    void enterChatRoom(String roomId, String co_email, ChatMessage chatMessage);
    void closeChatRoom(String roomId, String co_email);
    CoDevResponse getChatRoom(String roomId);
    CoDevResponse exitChatRoom(String co_email, String roomId);
    void sendMessage(ChatMessage chatMessage, SimpMessageSendingOperations sendingOperations) throws ParseException;
    CoDevResponse updateRoomTitle(String room_title, String roomId);
    CoDevResponse confirmRoom(HttpServletRequest request, String roomId);
    CoDevResponse getParticipantsOfRoom(HttpServletRequest request, String roomId);
}
