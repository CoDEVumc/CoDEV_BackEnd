package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPart {
    private String co_part;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
