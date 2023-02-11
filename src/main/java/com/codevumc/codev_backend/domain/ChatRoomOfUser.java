package com.codevumc.codev_backend.domain;

import java.sql.Timestamp;

public class ChatRoomOfUser {

    private String roomId;
    private String co_email;
    private String co_nickName;
    private String profileImg;
    private int isRead; //메시지 읽음 처리
    private boolean status; //현재 채팅방에 존재하는 지 여부
    private Timestamp updatedAt; //마지막으로 메시지 받은 시간

}
