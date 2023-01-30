package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.service.co_chat.CoChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoChatController {
    private final SimpMessageSendingOperations sendingOperations;
    private final CoChatServiceImpl coChatService;

    @Autowired
    public CoChatController(SimpMessageSendingOperations sendingOperations, CoChatServiceImpl coChatService) {
        this.sendingOperations = sendingOperations;
        this.coChatService = coChatService;
    }

    @MessageMapping("/chat/enter")
    public ChatMessage enter(ChatMessage chatMessage) {
        System.out.println("enter method");
        //chatService.enterRoom(chatMessage);
        //chatService.readMessage(chatMessage);
        sendingOperations.convertAndSend("/topic/chat/room/"+ chatMessage.getRoomId(), chatMessage);

        return chatMessage;
    }

    @MessageMapping("/chat/exit")
    public ChatMessage exit(ChatMessage chatMessage) {
        //chatService.exitRoom(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/chat/message")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        //chatService.insertMessage(chatMessage);
        sendingOperations.convertAndSend("/topic/chat/room/"+ chatMessage.getRoomId(), chatMessage);
        return chatMessage;
    }

    @MessageMapping("/alarm/message")
    public ChatMessage messageGreeting(ChatMessage chatMessage) {
        //chatMessage.setStatus(chatService.getReceiverStatus(chatMessage).getStatus());
        if(chatMessage.getStatus() == 0) {
            //chatService.noneReadMessage(chatMessage);
            sendingOperations.convertAndSend("/topic/chat/alarm/"+ chatMessage.getReceiver(), chatMessage);
        }

        return null;
    }
}
