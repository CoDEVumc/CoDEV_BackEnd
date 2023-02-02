package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoRecruitOfStudyPortfolio {
    private long co_studyId;
    private long co_portfolioId;
    private String co_email;
    private String profileImg;
    private String co_name;
    private String co_gender;
    private String co_birth;
    private String co_title;
    private String co_rank;
    private String co_headLine;
    private String co_introduction;
    private String co_motivation;
    private String co_links;
    private String co_languages;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
