package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoPartOfProject {
    private String co_part;
    private int co_limit;
}
