package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoProject {
    private long co_projectId;
    private String co_email;
    private String co_title;
    private String co_content;
    private String co_mainImg;
    private int co_limit;
    private boolean co_process;
    private Timestamp createdAt;
    private Timestamp updateAt;
    private boolean status;
}
