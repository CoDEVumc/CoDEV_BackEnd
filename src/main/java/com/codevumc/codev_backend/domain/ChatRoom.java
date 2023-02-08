package com.codevumc.codev_backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
public class ChatRoom {
    private String roomId;
    private RoomType room_type;
    private String room_title;
    private boolean isRead;
    private boolean status;
    private Timestamp createdDate;
    private List<ChatRoomOfUser> coChatOfUserList;

    public enum RoomType {
        OTO("OTO"),// One To One
        OTM("OTM");// One To Many

        private String value;

        RoomType(String value) {
            this.value=value;
        }

        public static ChatRoom.RoomType from(String s) {
            return ChatRoom.RoomType.valueOf(s.toUpperCase());
        }
        public String getValue() { return this.value; }
    }
}
