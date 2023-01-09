package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoPartOfStudy {
    private long co_posId;
    private long co_studyId;
    private long co_partId;
    private Timestamp createAt;
    private Timestamp updateAt;
    private boolean status;

}
