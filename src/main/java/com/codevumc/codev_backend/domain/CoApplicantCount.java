package com.codevumc.codev_backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoApplicantCount {
    private String co_part;
    private int co_limit;
    private int co_applicantsCount;
}
