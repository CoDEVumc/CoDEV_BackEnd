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
    private String co_writer;
    private long co_projectId;
    private long co_portfolioId;
    private String co_partId;
    private String co_motivation;
    private CoProject.DevType co_process;
    private boolean isApproved;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean co_recruitStatus;
    private boolean status;

}
