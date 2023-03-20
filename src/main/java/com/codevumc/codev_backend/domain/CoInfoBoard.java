package com.codevumc.codev_backend.domain;


import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoInfoBoard {
    private long co_infoId;
    private String co_email;
    private String co_viewer;
    private String co_title;
    private String content;
    private String co_mainImg;
    private int commentCount;
    private boolean co_mark;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CoPhotos> co_photos;
    private List<CoCommentOfInfoBoard> co_comment;
}
