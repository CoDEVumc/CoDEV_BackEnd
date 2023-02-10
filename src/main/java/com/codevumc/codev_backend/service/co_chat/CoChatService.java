package com.codevumc.codev_backend.service.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

import java.sql.Timestamp;
import java.text.ParseException;

public interface CoChatService {
    CoDevResponse createChatRoom(ChatRoom chatRoom);
    CoDevResponse getChatRooms(String co_email);
    CoDevResponse inviteUser(String roomId, JSONArray co_emails, String self_email);
    void enterChatRoom(String roomId, String co_email, ChatMessage chatMessage);
    void closeChatRoom(String roomId, String co_email);
    CoDevResponse getChatRoom(String roomId);
    CoDevResponse exitChatRoom(String co_email, String roomId);
    int sendMessage(ChatMessage chatMessage) throws ParseException;
    CoDevResponse getChatLog(String roomId);
}
