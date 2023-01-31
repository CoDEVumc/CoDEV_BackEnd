package com.codevumc.codev_backend.service.co_chat;

import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoChatService {
    CoDevResponse createChatRoom(ChatRoom chatRoom);
    CoDevResponse getChatRooms(String co_email);
    CoDevResponse inviteUser(String roomId, JSONArray co_emails);
    void enterChatRoom(String roomId, String co_email);
    void closeChatRoom(String roomId, String co_email);
    CoDevResponse getChatRoom(String roomId);
    CoDevResponse exitChatRoom(String co_email, String roomId);
    void sendMessage(String roomId);
}
