package com.codevumc.codev_backend.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoApplicantInfo {
    private String co_part;
    private int co_limit;
    private List<CoUserInfo> co_applicants;
}
