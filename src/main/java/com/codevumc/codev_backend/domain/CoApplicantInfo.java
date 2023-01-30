package com.codevumc.codev_backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CoApplicantInfo {
    private long co_portfolioId;
    private String co_email;
    private String co_title;
    private String co_name;
    private String profileImg;
    private String co_part;
    private boolean co_temporaryStorage;
    private Timestamp createdAt;
}
