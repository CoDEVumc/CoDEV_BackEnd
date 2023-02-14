package com.codevumc.codev_backend.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatMessage")
public class ChatMessage {

    private MessageType type;
    private String roomId;
    private String sender;
    private String co_nickName;
    private String profileImg;
    private String content;
    private boolean isPm;
    private String createdDate;

    public enum MessageType {
        ENTER("ENTER"),
        TALK("TALK"),
        LEAVE("LEAVE"),
        EXIT("EXIT"),
        INVITE("INVITE"),
        DAY("DAY"),
        TAB("TAB");

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
