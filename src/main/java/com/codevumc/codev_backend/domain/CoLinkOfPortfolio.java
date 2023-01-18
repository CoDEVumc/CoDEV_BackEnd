package com.codevumc.codev_backend.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLinkOfPortfolio {
    private long co_lopId;
    private long co_portfolioId;
    private String co_link;
}
