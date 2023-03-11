package com.codevumc.codev_backend.domain;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoCommentOfInfoBoard {
    private long co_coib;
    private String co_email;
    private long co_infoId;
    private String content;
    private Timestamp createdAt;
}
