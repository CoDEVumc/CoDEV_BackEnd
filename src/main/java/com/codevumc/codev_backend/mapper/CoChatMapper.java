package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.domain.ChatRoomOfUser;
import com.codevumc.codev_backend.domain.CoChatOfUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoChatMapper {
    //채팅방 만들기
    void createChatRoom(ChatRoom chatRoom);

    //채팅방 목록 조회
    List<ChatRoom> getChatRooms(@Param("co_email") String co_email);

    //채팅방 초대
    void inviteUser(@Param("roomId") String roomId, @Param("co_email") String co_email);

    //채팅방 입장
    void readMessage(@Param("roomId") String roomId, @Param("co_email") String co_email);

    //채팅방 입장
    void enterChatRoom(@Param("roomId") String roomId, @Param("co_email") String co_email);

    //채팅방 닫기
    void closeChatRoom(@Param("roomId") String roomId, @Param("co_email") String co_email);

    //메시지 보내기
    void sendMessage(@Param("roomId") String roomId);

    //채팅방 열기
    Optional<ChatRoomOfUser> getChatRoom(@Param("roomId") String roomId);

    //채팅방 나가기
    void exitChatRoom(@Param("co_email") String co_email, @Param("roomId") String roomId);
}