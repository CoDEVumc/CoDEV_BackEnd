package com.codevumc.codev_backend.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLanguageOfPortfolio {
    private long co_lopfId;
    private long co_portfolioId;
    private long co_languageId;
    private String co_language;
    private String co_logo;
}
