package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoApplicantInfo {
    private String co_part;
    private int co_limit;
    private List<CoUser> co_applicants;
}
