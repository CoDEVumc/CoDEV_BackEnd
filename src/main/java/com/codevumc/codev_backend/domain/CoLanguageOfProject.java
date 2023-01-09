package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLanguageOfProject {
    private long co_lopId;
    private long co_projectId;
    private long co_languageId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
