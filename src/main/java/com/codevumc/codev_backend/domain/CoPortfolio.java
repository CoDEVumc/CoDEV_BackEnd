package com.codevumc.codev_backend.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPortfolio {
    private long co_portfolioId;
    private long co_lopfId;
    private String co_email;
    private String co_name;
    private String co_gender;
    private String co_birth;
    private String co_rank;
    private String co_headLine;
    private String co_introduction;
    private List<String> co_portfolioLanguage;
    private List<String> co_portfolioLink;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
