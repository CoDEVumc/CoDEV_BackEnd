package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_chat.CoChatService;
import com.codevumc.codev_backend.service.co_chat.CoChatServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.codevumc.codev_backend.service.firebase.FirebaseCloudMessageService;
import com.codevumc.codev_backend.service.firebase.FirebaseCloudMessageServiceImpl;
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
    private final CoChatService coChatService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Autowired
    public CoChatController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, SimpMessageSendingOperations sendingOperations, CoChatServiceImpl coChatService, FirebaseCloudMessageServiceImpl firebaseCloudMessageService) {
        super(jwtTokenProvider, jwtService);
        this.sendingOperations = sendingOperations;
        this.coChatService = coChatService;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
    }

    @MessageMapping("/chat/message")
    public ChatMessage message(@Payload String data) throws ParseException, java.text.ParseException {
        ChatMessage chatMessage = getChatMessage(data);
        if(ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            coChatService.enterChatRoom(chatMessage.getRoomId(), chatMessage.getSender(), chatMessage);
            sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.getRoomId(), chatMessage);
        } else if(ChatMessage.MessageType.TALK.equals(chatMessage.getType())) {
            coChatService.sendMessage(firebaseCloudMessageService, chatMessage, sendingOperations);
        }else if(ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSender() + "");
            coChatService.closeChatRoom(chatMessage.getRoomId(), chatMessage.getSender());
            sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.getRoomId(), chatMessage);
        }

        return chatMessage;
    }


    @PostMapping("/create/room")
    public CoDevResponse createChatRoom(HttpServletRequest request, @RequestBody Map<String, String> chat) throws Exception {

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chat.get("roomId"))
                .room_type(ChatRoom.RoomType.valueOf(chat.get("room_type")))
                .room_title(chat.get("room_title"))
                .build();
        if(chat.get("mainImg") != null)
            chatRoom.setMainImg(chat.get("mainImg"));

        return coChatService.createChatRoom(request, chatRoom, getCoUserEmail(request));
    }

    @GetMapping("/rooms")
    public CoDevResponse getChatRooms(HttpServletRequest request) throws Exception{
        return coChatService.getChatRooms(getCoUserEmail(request));
    }


    @PostMapping("/invite")
    public CoDevResponse inviteUser(HttpServletRequest request, @RequestBody Map<String, Object> user) throws Exception{
        return coChatService.inviteUser(user.get("roomId").toString(), getJSONArray(user.get("co_emails")));
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

    @PostMapping("/confirm/{roomId}")
    public CoDevResponse confirmRoom(HttpServletRequest request, @PathVariable("roomId") String roomId) {
        return coChatService.confirmRoom(request, roomId);
    }

    @PostMapping("/update/room_title")
    public CoDevResponse updateRoomTitle(HttpServletRequest request, @RequestBody Map<String, Object> user) {
        return coChatService.updateRoomTitle(user.get("room_title").toString(), user.get("roomId").toString());
    }

    @GetMapping("/participants/{roomId}")
    public CoDevResponse getParticipants(HttpServletRequest request, @PathVariable("roomId") String roomId) {
        return coChatService.getParticipantsOfRoom(request, roomId);
    }

    private ChatMessage getChatMessage(String data) throws ParseException{
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(data);
            CoUser coUser = coChatService.getUserInfo(jsonObject.get("sender").toString());
        String[] classification = jsonObject.get("roomId").toString().split("_");
        boolean isPm = false;

        if(classification[0].equals("OTM"))
            isPm = coChatService.isBoardAdmin(classification[1], Long.parseLong(classification[2]), jsonObject.get("sender").toString());

        return ChatMessage.builder()
                .type(ChatMessage.MessageType.valueOf(jsonObject.get("type").toString()))
                .roomId(jsonObject.get("roomId").toString())
                .sender(jsonObject.get("sender").toString())
                .co_nickName(coUser.getCo_nickName())
                .profileImg(coUser.getProfileImg())
                .content(jsonObject.get("content").toString())
                .isPm(isPm)
                .createdDate(sdf.format(timestamp)).build();
    }

    private JSONArray getJSONArray(Object object) throws Exception{
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(object);
        return (JSONArray) parser.parse(jsonArray);
    }


}
