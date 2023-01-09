package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoPartOfProject {
    private long co_popId;
    private long co_projectId;
    private long co_partId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
