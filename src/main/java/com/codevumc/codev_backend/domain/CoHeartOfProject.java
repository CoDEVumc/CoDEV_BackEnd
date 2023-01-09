package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoHeartOfProject {
    private long co_hopId;
    private String co_email;
    private long co_projectId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
