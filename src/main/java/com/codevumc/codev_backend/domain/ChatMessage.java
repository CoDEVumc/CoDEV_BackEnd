package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private MessageType type;
    private String roomId;
    private String sender;
    private String content;
    private Timestamp createDate;

    public enum MessageType {
        ENTER("ENTER"),
        TALK("TALK"),
        LEAVE("LEAVE"),
        EXIT("EXIT");

        private String value;

        MessageType(String value) {
            this.value=value;
        }

        public static MessageType from(String s) {
            return MessageType.valueOf(s.toUpperCase());
        }
        public String getValue() { return this.value; }
    }
}
