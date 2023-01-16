package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPart {
    private String co_part;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
