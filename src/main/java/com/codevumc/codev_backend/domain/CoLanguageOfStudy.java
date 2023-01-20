package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLanguageOfStudy {
    private long co_losId;
    private long co_studyId;
    private long co_languageId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
