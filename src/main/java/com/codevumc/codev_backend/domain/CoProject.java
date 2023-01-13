package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoProject {
    private long co_projectId;
    private String co_email;
    private String co_title;
    private String co_location;
    private String co_content;
    private String co_mainImg;
    private boolean co_process;
    private Timestamp co_deadLine;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
    private List<CoPhotos> coPhotos;
}
