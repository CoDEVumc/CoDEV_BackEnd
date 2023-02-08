package com.codevumc.codev_backend.domain;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private RoomType room_type;
    private String room_title;
    private boolean isRead;
    private boolean status;
    private String receiverCo_email;
    private String receiverCo_nickName;
    private String receiverProfileImg;
    private int people;
    private String latestconv;
    private String latestDate;

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
