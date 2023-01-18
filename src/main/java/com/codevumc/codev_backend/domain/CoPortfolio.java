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
    private String co_title;
    private String co_name;
    private String co_gender;
    private String co_birth;
    private String co_rank;
    private String co_headLine;
    private String co_introduction;
    private String co_parts;
    private String co_languages;
    private String co_links;
    private List<String> co_partList;
    private List<String> co_languageList;
    private List<String> co_linkList;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
