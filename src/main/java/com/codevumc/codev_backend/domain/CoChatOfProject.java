package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class CoChatOfProject {
    private String co_email;
    private String co_chatName;
}
