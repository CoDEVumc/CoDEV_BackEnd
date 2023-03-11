package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoMarkOfInfoBoard {
    private long co_moib;
    private String co_email;
    private long co_infoId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
