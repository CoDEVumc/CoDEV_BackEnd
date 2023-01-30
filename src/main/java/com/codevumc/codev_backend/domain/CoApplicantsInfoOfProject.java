package com.codevumc.codev_backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CoApplicantsInfoOfProject {
    private List<CoPartOfProject> co_recruitmentStatus;
    private String co_part;
    private List<CoApplicantInfo> co_appllicantsInfo;
}
