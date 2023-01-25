package com.codevumc.codev_backend.domain;

import lombok.Setter;

import java.util.List;

@Setter
public class CoApplicantInfo {
    private String co_part;
    private int co_limit;
    private List<CoUser> co_applicants;
}
