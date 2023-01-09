package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPartOfStudy {
    private long co_posId;
    private long co_studyId;
    private long co_partId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;

}
