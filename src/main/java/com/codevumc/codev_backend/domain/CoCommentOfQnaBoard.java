package com.codevumc.codev_backend.domain;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoCommentOfQnaBoard {
    private long co_coqb;
    private String co_email;
    private long co_qnaId;
    private String content;
    private Timestamp createdAt;
}
