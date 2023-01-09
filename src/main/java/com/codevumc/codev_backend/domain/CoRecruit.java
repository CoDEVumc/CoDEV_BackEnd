package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoRecruit {
    private long co_recruitId;
    private String co_email;
    private long co_projectId;
    private long co_partId;
    private boolean isApproved;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
