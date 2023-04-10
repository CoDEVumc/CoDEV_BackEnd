package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoReCommentOfQnaBoard {
    private long co_rcoqb;
    private String co_email;
    private String co_nickname;
    private String profileImg;
    private long co_coqb;
    private String content;
    private Timestamp createdAt;
}
