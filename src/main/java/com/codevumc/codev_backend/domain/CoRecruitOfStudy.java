package com.codevumc.codev_backend.domain;

import com.codevumc.codev_backend.domain.CoStudy.DevType;
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
    private String co_writer;
    private long co_studyId;
    private long co_portfolioId;
    private String co_motivation;
    private DevType co_process;
    private boolean isApproved;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean co_recruitStatus;
    private boolean status;
}
