package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoMarkOfQnaBoard {
    private long co_moqb;
    private String co_email;
    private long co_qnaId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
