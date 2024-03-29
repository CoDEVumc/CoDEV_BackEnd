package com.codevumc.codev_backend.domain;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLikeOfInfoBoard {
    private long co_loib;
    private String co_email;
    private long co_infoId;
    private boolean co_like;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
