package com.codevumc.codev_backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CoApplicantsInfoOfStudy {
    private String co_part;
    private int co_tempSavedApplicantsCount;
    private CoApplicantCount co_applicantCount;
    private List<CoApplicantInfo> co_applicantsInfo;
}
