package com.codevumc.codev_backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Setter
@Getter
public class ChatRoomOfUser {

    private String roomId;
    private String co_email;
    private String co_nickName;
    private String profileImg;
    private int isRead;
    private boolean isPm;//메시지 읽음 처리
    private boolean status; //현재 채팅방에 존재하는 지 여부
    private Timestamp updatedAt; //마지막으로 메시지 받은 시간

}
