package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLanguage {
    private long co_languageId;
    private String co_language;
    private String co_logo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

