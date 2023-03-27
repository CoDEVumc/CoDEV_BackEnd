package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoReCommentOfInfoBoard {
    private long co_rcoib;
    private String co_email;
    private String co_nickname;
    private long co_coib;
    private String content;
    private Timestamp createdAt;
}
