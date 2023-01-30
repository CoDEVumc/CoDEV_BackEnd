package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.domain.CoChat;
import com.codevumc.codev_backend.domain.CoChatOfUser;
import com.codevumc.codev_backend.mapper.CoChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatTestService {
    private final CoChatMapper coChatMapper;

    @Autowired
    public ChatTestService(CoChatMapper chatMapper) {
        this.coChatMapper = chatMapper;
    }

    public void inviteUser(CoChatOfUser coChatOfUser) {
        this.coChatMapper.inviteUser(coChatOfUser);
    }

    public List<CoChatOfUser> getChatRoomList(String co_email) {
        return this.coChatMapper.getChatRoomList(co_email);
    }

    public CoChatOfUser getChatRoom(String roomId, String co_type, String co_email) {
        Optional<CoChatOfUser> coChatOfUser = this.coChatMapper.getChatRoom(roomId, co_type, co_email);
        return coChatOfUser.orElse(null);
    }

    public void exitChatRoom(String roomId, String co_type, String co_email) {
        CoChatOfUser coChatOfUser = getChatRoom(roomId, co_type, co_email);
        if(coChatOfUser != null)
            this.coChatMapper.exitChatRoom(roomId, co_type, co_email);
    }

    public void readMessage(String roomId, String co_type, String co_email) {
        CoChatOfUser coChatOfUser = getChatRoom(roomId, co_type, co_email);
        if(coChatOfUser != null)
            this.coChatMapper.readMessage(roomId, co_type, co_email);
    }

    public void sendMessage(String roomId, String co_type) {
        this.coChatMapper.sendMessage(roomId, co_type);
    }


}
