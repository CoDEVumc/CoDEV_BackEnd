package com.codevumc.codev_backend.domain;


import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoCommentOfInfoBoard {
    private long co_coib;
    private String co_email;
    private String co_nickname;
    private String profileImg;
    private long co_infoId;
    private String content;
    private Timestamp createdAt;
    private List<CoReCommentOfInfoBoard> coReCommentOfInfoBoardList;
}
