package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
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

