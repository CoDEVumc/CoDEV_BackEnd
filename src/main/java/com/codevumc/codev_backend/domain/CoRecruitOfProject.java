package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoRecruitOfProject {
    private long ropId;
    private String co_email;
    private long co_projectId;
    private long co_portfolioId;
    private String co_partId;
    private String co_motivation;
    private boolean isApproved;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
