package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoChatOfUser {
    private String roomId;
    private String co_type;
    private String co_mainImg;
    private String title;
    private String co_email;
    private boolean isRead;
    private boolean status;
}
