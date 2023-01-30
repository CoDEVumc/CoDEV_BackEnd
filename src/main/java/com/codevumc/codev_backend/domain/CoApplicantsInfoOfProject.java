package com.codevumc.codev_backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CoApplicantsInfoOfProject {
    private List<CoPartOfProject> co_recruitmentStatus;
    private String co_part;
    private List<CoPortfolio> co_apllicants;
}
