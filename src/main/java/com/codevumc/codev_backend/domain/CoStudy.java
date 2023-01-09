package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoStudy {
    private long co_studyId;
    private String co_email;
    private String co_title;
    private String content;
    private String co_logo;
    private String co_limit;
    private boolean co_process;
    private Timestamp createrdAt;
    private Timestamp updatedAt;
    private boolean status;
}
