package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoPart {
    private long co_partId;
    private String co_part;
    private Timestamp createAt;
    private Timestamp updateAt;
}
