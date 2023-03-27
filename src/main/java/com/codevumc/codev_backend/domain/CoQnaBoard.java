package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoQnaBoard {
    private long co_qnaId;
    private String co_email;
    private String co_viewer;
    private String co_nickname;
    private String profileImg;
    private String co_title;
    private String content;
    private String co_mainImg;
    private Long co_likeCount;
    private Long co_commentCount;
    private Long co_markCount;
    private boolean co_mark;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CoPhotos> co_photos;
    private List<CoCommentOfQnaBoard> co_comment;
}
