package com.codevumc.codev_backend.service.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.mapper.CoChatMapper;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoChatServiceImpl implements CoChatService{
    private final CoChatMapper coChatMapper;

    @Autowired
    public CoChatServiceImpl(CoChatMapper coChatMapper) {
        this.coChatMapper = coChatMapper;
    }
/*

    @Transactional
    public void insertMessage(ChatMessage chatMessage) {
        coChatMapper.insertMessage(chatMessage);
    }

    @Transactional
    public void enterRoom(ChatMessage chatMessage) {
        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setRoomId(chatMessage.getRoomId());
        chatRoomUser.setUserId(chatMessage.getSender());
        coChatMapper.enterRoom(chatRoomUser);
    }
    public void exitRoom(ChatMessage chatMessage) {
        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setRoomId(chatMessage.getRoomId());
        chatRoomUser.setUserId(chatMessage.getSender());
        coChatMapper.exitRoom(chatRoomUser);
    }

    public ChatRoomUser getReceiverStatus(ChatMessage chatMessage) {
        return chatMapper.getChatRoomUser(chatMessage);
    }

    public void readMessage(ChatMessage chatMessage) {
        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setUserId(chatMessage.getSender());
        chatRoomUser.setRoomId(chatMessage.getRoomId());
        coChatMapper.readMessage(chatRoomUser);
    }

    public void noneReadMessage(ChatMessage chatMessage) {
        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setUserId(chatMessage.getReceiver());
        chatRoomUser.setRoomId(chatMessage.getRoomId());
        coChatMapper.noneReadMessage(chatRoomUser);
    }
*/

}
