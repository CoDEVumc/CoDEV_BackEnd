package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoChatOfUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoChatMapper {
    void inviteUser(CoChatOfUser coChatOfUser);

    List<CoChatOfUser> getChatRoomList(@Param("co_email") String co_email);

    Optional<CoChatOfUser> getChatRoom(@Param("roomId") String roomId, @Param("co_type") String co_type, @Param("co_email") String co_email);

    void exitChatRoom(@Param("roomId") String roomId, @Param("co_type") String co_type, @Param("co_email") String co_email);

    void readMessage(@Param("roomId") String roomId, @Param("co_type") String co_type, @Param("co_email") String co_email);

    void sendMessage(@Param("roomId") String roomId, @Param("co_type") String co_type);
}
