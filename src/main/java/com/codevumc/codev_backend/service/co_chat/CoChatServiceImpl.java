package com.codevumc.codev_backend.service.co_chat;

import com.codevumc.codev_backend.domain.ChatMessage;
import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoChatMapper;
import com.codevumc.codev_backend.mongo_repository.ChatMessageRepository;
import com.codevumc.codev_backend.service.ResponseService;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public CoDevResponse createChatRoom(ChatRoom chatRoom) {
        try {
            coChatMapper.createChatRoom(chatRoom);
            return setResponse(200, "message", "채팅방이 생성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
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
    public CoDevResponse inviteUser(String roomId, JSONArray co_emails, String self_email) {
        try {
            coChatMapper.inviteUser(roomId, self_email);
            for(Object email : co_emails) {
                String co_email = (String) email;
                coChatMapper.inviteUser(roomId, co_email);
            }
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
        chatMessageRepository.save(chatMessage);
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
    public int sendMessage(ChatMessage chatMessage) throws ParseException {
        //읽음 처리용
        coChatMapper.sendMessage(chatMessage.getRoomId());
        //MongoDB 채팅 내용 저장
        chatMessageRepository.save(chatMessage);
        String lastDay = chatMessageRepository.findTopByRoomIdAndTypeOrderByCreatedDateDesc(chatMessage.getRoomId(), ChatMessage.MessageType.TALK.getValue()).getCreatedDate();
        if(lastDay != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(lastDay).compareTo(sdf.parse(chatMessage.getCreatedDate()));
        }
        return 0;
    }

    @Override
    public CoDevResponse getChatLog(String roomId) {
        try {
            //List<ChatMessage> chatMessageList = chatMessageRepository.findByRoomId(roomId);
            //if(!chatMessageList.isEmpty())
            //  return setResponse(200, "chatLog", chatMessageList);
            return setResponse(200, "chatLog", "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    public CoUser getUserInfo(String co_email) {
        return coChatMapper.getUserInfo(co_email);
    }

}
