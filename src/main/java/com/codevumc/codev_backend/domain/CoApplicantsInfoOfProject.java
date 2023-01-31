package com.codevumc.codev_backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CoApplicantsInfoOfProject {
    private String co_part;
    private int co_tempSavedApplicantsCount;
    private List<CoApplicantCount> co_applicantsCount;
    private List<CoApplicantInfo> co_appllicantsInfo;
}
