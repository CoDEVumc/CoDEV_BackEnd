package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoLanguageOfStudy {
    private long co_losId;
    private long co_studyId;
    private long co_languageId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
