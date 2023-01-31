package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_chat.CoChatServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/codev/chat")
@RestController
public class CoChatController extends JwtController {
    private final SimpMessageSendingOperations sendingOperations;
    private final CoChatServiceImpl coChatService;

    @Autowired
    public CoChatController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, SimpMessageSendingOperations sendingOperations, CoChatServiceImpl coChatService) {
        super(jwtTokenProvider, jwtService);
        this.sendingOperations = sendingOperations;
        this.coChatService = coChatService;
    }

    @MessageMapping("/chat/message")
    public ChatMessage message(@Payload ChatMessage chatMessage) {
        if(ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            coChatService.enterChatRoom(chatMessage.getRoomId(), chatMessage.getSender());
        }else if(ChatMessage.MessageType.TALK.equals(chatMessage.getType())) {
            coChatService.sendMessage(chatMessage.getRoomId());
        }else if(ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSender() + "");
            coChatService.closeChatRoom(chatMessage.getRoomId(), chatMessage.getSender());
        }

        sendingOperations.convertAndSend("/sub/chat/room/"+chatMessage.getRoomId(), chatMessage);
        return chatMessage;
    }


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        //chatService.insertMessage(chatMessage);
        sendingOperations.convertAndSend("/topic/chat/room/"+ chatMessage.getRoomId(), chatMessage);
        return chatMessage;
    }

    @PostMapping("/create/room")
    public CoDevResponse createChatRoom(HttpServletRequest request, @RequestBody Map<String, String> chat) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chat.get("roomId"))
                .room_type(ChatRoom.RoomType.valueOf(chat.get("room_type")))
                .room_title(chat.get("co_title"))
                .room_mainImg(chat.get("co_mainImg"))
                .build();
        return coChatService.createChatRoom(chatRoom);
    }

    @GetMapping("/rooms")
    public CoDevResponse getChatRooms(HttpServletRequest request) throws Exception{
        return coChatService.getChatRooms(getCoUserEmail(request));
    }

    @PostMapping("/invite")
    public CoDevResponse inviteUser(@RequestBody Map<String, Object> user) throws Exception{
        return coChatService.inviteUser(user.get("roomId").toString(), getJSONArray(user.get("co_emails")));
    }

    @GetMapping("/room/{roomId}")
    public CoDevResponse getChatRoom(@PathVariable("roomId") String roomId) {
        return coChatService.getChatRoom(roomId);
    }

    @DeleteMapping("/delete/{roomId}")
    public CoDevResponse exitChatRoom(HttpServletRequest request, @PathVariable("roomId") String roomId) throws Exception{
        String co_email = getCoUserEmail(request);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(co_email);
        chatMessage.setType(ChatMessage.MessageType.EXIT);
        chatMessage.setRoomId(roomId);

        //해당 채팅방에다가 나갔다는 메시지 전달
        sendingOperations.convertAndSend("/sub/chat/room/"+roomId, chatMessage);
        return coChatService.exitChatRoom(co_email, roomId);
    }

    private JSONArray getJSONArray(Object object) throws Exception{
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(object);
        return (JSONArray) parser.parse(jsonArray);
    }


}
