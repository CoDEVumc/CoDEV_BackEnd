package com.codevumc.codev_backend.service.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.domain.ChatRoomOfUser;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoChatMapper;
import com.codevumc.codev_backend.mongo_repository.ChatMessageRepository;
import com.codevumc.codev_backend.service.ResponseService;
import org.json.simple.JSONArray;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CoChatServiceImpl extends ResponseService implements CoChatService{
    private final CoChatMapper coChatMapper;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public CoChatServiceImpl(CoChatMapper coChatMapper, ChatMessageRepository chatMessageRepository) {
        this.coChatMapper = coChatMapper;
        this.chatMessageRepository = chatMessageRepository;

    }

    @Override
    public CoDevResponse createChatRoom(HttpServletRequest request, ChatRoom chatRoom, String self_email) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            coChatMapper.createChatRoom(chatRoom);
            coChatMapper.inviteUser(chatRoom.getRoomId(), self_email, timestamp);
            coChatMapper.inviteTemp(chatRoom.getRoomId(), "TEMP");
            return setResponse(200, "message", "채팅방이 생성되었습니다.");
        } catch (Exception e) {
            request.setAttribute("exception", "DUPLICATEERROR");
            throw new AuthenticationCustomException(ErrorCode.DUPLICATEERROR);
        }
    }

    @Override
    public CoDevResponse getChatRooms(String co_email) {
        try {
            List<ChatRoom> chatRooms = coChatMapper.getChatRooms(co_email);
            ChatMessage chatMessage;
            for(ChatRoom chatRoom : chatRooms) {
                chatMessage = chatMessageRepository.findTopByRoomIdAndTypeOrderByCreatedDateDesc(chatRoom.getRoomId(), ChatMessage.MessageType.TALK.getValue());
                chatRoom.setLatestconv(chatMessage != null ? chatMessage.getContent() : null);
                chatRoom.setLatestDate(chatMessage != null ? chatMessage.getCreatedDate() : null);

            }
            return setResponse(200, "complete", chatRooms);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse inviteUser(String roomId, JSONArray co_emails) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            for(Object email : co_emails) {
                String co_email = (String) email;
                coChatMapper.inviteUser(roomId, co_email, timestamp);
            }
            coChatMapper.updateTime(new Timestamp(System.currentTimeMillis()), roomId);
            return setResponse(200, "message", "채팅방에 초대하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public void enterChatRoom(String roomId, String co_email, ChatMessage chatMessage) {
        coChatMapper.readMessage(roomId, co_email);
        coChatMapper.enterChatRoom(roomId, co_email);
        //chatMessageRepository.save(chatMessage);
    }

    @Override
    public void closeChatRoom(String roomId, String co_email) {
        coChatMapper.closeChatRoom(roomId, co_email);
    }

    @Override
    public CoDevResponse getChatRoom(String roomId) {
        try {

            List<ChatMessage> chatMessageList = chatMessageRepository.findAllByRoomIdOrderByCreatedDate(roomId);
            return !chatMessageList.isEmpty() ?  setResponse(200, "complete", chatMessageList) : setResponse(404, "message", "메시지가 없습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse exitChatRoom(String co_email, String roomId) {
        try {
            coChatMapper.exitChatRoom(co_email, roomId);
            return setResponse(200, "message", "채팅방을 나갔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public void sendMessage(ChatMessage chatMessage, SimpMessageSendingOperations sendingOperations) throws ParseException {
        //읽음 처리용
        coChatMapper.sendMessage(chatMessage.getRoomId());

        ChatMessage latestDate = chatMessageRepository.findTopByRoomIdAndTypeOrderByCreatedDateDesc(chatMessage.getRoomId(), ChatMessage.MessageType.TALK.getValue());

        String lastDay = latestDate != null ? latestDate.getCreatedDate() : "0000-00-00";


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        if(sdf.parse(lastDay).before(sdf.parse(chatMessage.getCreatedDate()))) {
            ChatMessage notice = ChatMessage.builder()
                    .type(ChatMessage.MessageType.DAY)
                    .roomId(chatMessage.getRoomId())
                    .content(now())
                    .createdDate(chatMessage.getCreatedDate()).build();
            chatMessageRepository.save(notice);
            sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.getRoomId(), notice);

            if(lastDay.equals("0000-00-00")) {
                StringBuffer sb = new StringBuffer();
                sb.append(chatMessage.getCo_nickName());
                sb.append("님이 ");
                List<String> co_nickNames = coChatMapper.getNickNames(chatMessage.getRoomId(), chatMessage.getSender());
                for(int i = 0; i < co_nickNames.size(); i++) {
                    sb.append(co_nickNames.get(i));
                    sb.append("님");
                    if(i != co_nickNames.size() - 1)
                        sb.append(", ");
                }
                sb.append("을 초대했습니다.");
                notice.setType(ChatMessage.MessageType.INVITE);
                notice.setContent(sb.toString());
                chatMessageRepository.save(notice);
                sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.getRoomId(), notice);
            }

        }

        chatMessageRepository.save(chatMessage);
        coChatMapper.updateTime(new Timestamp(System.currentTimeMillis()), chatMessage.getRoomId());
    }


    @Override
    public CoDevResponse updateRoomTitle(String room_title, String roomId) {
        try {
            coChatMapper.updateRoomTitle(room_title, roomId);
            return setResponse(200, "message", "채팅방 이름이 변경되었습니다.");
        } catch (Exception e) {
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse confirmRoom(HttpServletRequest request, String roomId) {
        try {
            if(!coChatMapper.confirmRoom(roomId))
                return setResponse(200, "message", "방을 생성합니다.");
            throw new AuthenticationCustomException(ErrorCode.DUPLICATEERROR);
        } catch (Exception e) {
            request.setAttribute("exception", "DUPLICATEERROR");
            throw new AuthenticationCustomException(ErrorCode.DUPLICATEERROR);
        }
    }

    @Override
    public CoDevResponse getParticipantsOfRoom(HttpServletRequest request, String roomId) {
        try {
            List<ChatRoomOfUser> participants = coChatMapper.getParticipantsOfRoom(roomId);
            String[] classification = roomId.split("_");
            Map<String, Object> boardDto = new HashMap<>();
            String admin = null;
            if(classification[0].equals(ChatRoom.RoomType.OTM.getValue())) {
                boardDto.put("boardType", classification[1]);
                boardDto.put("co_boardId", classification[2]);
                admin = coChatMapper.getAdmin(boardDto);

                for(ChatRoomOfUser participant : participants) {
                    if(participant.getCo_email().equals(admin))
                        participant.setPm(true);
                }
            }
            return setResponse(200, "Complete", participants);
        } catch (Exception e) {
            request.setAttribute("exception", "REQUESTFAILED");
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    public CoUser getUserInfo(String co_email) {
        return coChatMapper.getUserInfo(co_email);
    }


    private String now() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREA);
        return sdf.format(timestamp);
    }

    public boolean isBoardAdmin(String boardType, long co_boardId, String co_email) {
        Map<String, Object> boardDto = new HashMap<String, Object>();
        boardDto.put("boardType", boardType);
        boardDto.put("co_boardId", co_boardId);
        boardDto.put("co_email", co_email);

        return coChatMapper.isBoardAdmin(boardDto);
    }
}
