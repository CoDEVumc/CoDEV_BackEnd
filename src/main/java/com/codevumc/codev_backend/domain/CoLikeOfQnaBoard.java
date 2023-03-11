package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLikeOfQnaBoard {
    private long co_loqb;
    private String co_email;
    private long co_qnaId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
