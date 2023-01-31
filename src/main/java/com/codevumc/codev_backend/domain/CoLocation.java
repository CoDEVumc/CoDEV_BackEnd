package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLocation {
    private String co_location;
    private boolean status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
