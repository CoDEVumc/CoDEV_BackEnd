package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.service.co_chat.CoChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat")
@RestController
public class CoChatController {
    private final SimpMessageSendingOperations sendingOperations;
    private final CoChatServiceImpl coChatService;

    @Autowired
    public CoChatController(SimpMessageSendingOperations sendingOperations, CoChatServiceImpl coChatService) {
        this.sendingOperations = sendingOperations;
        this.coChatService = coChatService;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage enter(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("enter method");
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        //chatService.enterRoom(chatMessage);
        //chatService.readMessage(chatMessage);
        //sendingOperations.convertAndSend("/topic/chat/room/"+ chatMessage.getRoomId(), chatMessage);

        return chatMessage;
    }


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        //chatService.insertMessage(chatMessage);
        sendingOperations.convertAndSend("/topic/chat/room/"+ chatMessage.getRoomId(), chatMessage);
        return chatMessage;
    }
}
