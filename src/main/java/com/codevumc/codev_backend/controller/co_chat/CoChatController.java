package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_chat.CoChatServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
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
    public ChatMessage message(@Payload String data) throws ParseException, java.text.ParseException {
        ChatMessage chatMessage = getChatMessage(data);
        if(ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            coChatService.enterChatRoom(chatMessage.getRoomId(), chatMessage.getSender(), chatMessage);
        } else if(ChatMessage.MessageType.TALK.equals(chatMessage.getType())) {
            ChatMessage nextDay = coChatService.sendMessage(chatMessage);
            if(nextDay != null)
                sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.getRoomId(), nextDay);
        }else if(ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSender() + "");
            coChatService.closeChatRoom(chatMessage.getRoomId(), chatMessage.getSender());
        }else if(ChatMessage.MessageType.INVITE.equals(chatMessage.getType())) {
            //초대 어떤 형태로??
        }

        sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.getRoomId(), chatMessage);
        return chatMessage;
    }


    @PostMapping("/create/room")
    public CoDevResponse createChatRoom(HttpServletRequest request, @RequestBody Map<String, String> chat) {

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chat.get("roomId"))
                .room_type(ChatRoom.RoomType.valueOf(chat.get("room_type")))
                .room_title(chat.get("co_title"))
                .build();
        return coChatService.createChatRoom(chatRoom);
    }

    @GetMapping("/rooms")
    public CoDevResponse getChatRooms(HttpServletRequest request) throws Exception{
        return coChatService.getChatRooms(getCoUserEmail(request));
    }

    @GetMapping("/test")
    public void test(@RequestBody Map<String, Object> user) throws java.text.ParseException {
        ChatMessage chatMessage = ChatMessage.builder()
                        .roomId(user.get("roomId").toString())
                        .sender(user.get("sender").toString())
                        .type(ChatMessage.MessageType.from(user.get("type").toString()))
                        .content(user.get("content").toString())
                        .createdDate(user.get("createdDate").toString()).build();
        coChatService.sendMessage(chatMessage);
    }

    @PostMapping("/invite")
    public CoDevResponse inviteUser(HttpServletRequest request, @RequestBody Map<String, Object> user) throws Exception{
        return coChatService.inviteUser(user.get("roomId").toString(), getJSONArray(user.get("co_emails")), getCoUserEmail(request));
    }

    @GetMapping("/room/{roomId}")
    public CoDevResponse getChatRoom(HttpServletRequest request, @PathVariable("roomId") String roomId) throws Exception{
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

    private ChatMessage getChatMessage(String data) throws ParseException{
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(data);
        CoUser coUser = coChatService.getUserInfo(jsonObject.get("sender").toString());
        return ChatMessage.builder()
                .type(ChatMessage.MessageType.valueOf(jsonObject.get("type").toString()))
                .roomId(jsonObject.get("roomId").toString())
                .sender(jsonObject.get("sender").toString())
                .co_nickName(coUser.getCo_nickName())
                .profileImg(coUser.getProfileImg())
                .content(jsonObject.get("content").toString())
                .createdDate(sdf.format(timestamp)).build();
    }

    private ChatMessage getChatMessage(String type, String roomId) throws ParseException {
        return ChatMessage.builder()
                .type(ChatMessage.MessageType.valueOf(type))
                .roomId(roomId)
                .sender("")
                .co_nickName("")
                .profileImg("")
                .content("")
                .createdDate("").
                build();
    }

    private JSONArray getJSONArray(Object object) throws Exception{
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(object);
        return (JSONArray) parser.parse(jsonArray);
    }


}
