package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoRecruitOfStudy {
    private long co_rosId;
    private String co_email;
    private long co_studyId;
    private long co_portfolioId;
    private boolean isApproved;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
