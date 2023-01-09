package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoLanguageOfUser {
    private long co_louId;
    private String co_email;
    private long co_languageId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
