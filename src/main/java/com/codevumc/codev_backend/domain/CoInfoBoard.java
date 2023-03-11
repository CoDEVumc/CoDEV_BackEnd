package com.codevumc.codev_backend.domain;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoInfoBoard {
    private long co_infoId;
    private String co_email;
    private String co_title;
    private String content;
    private String co_mainImg;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
