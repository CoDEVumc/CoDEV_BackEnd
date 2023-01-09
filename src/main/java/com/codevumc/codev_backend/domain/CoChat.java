package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoChat {
    private String co_chatName;
    private String chatType;
    private String co_chatFile;
    private Timestamp createdAt;
    private String co_chatImage;
}
