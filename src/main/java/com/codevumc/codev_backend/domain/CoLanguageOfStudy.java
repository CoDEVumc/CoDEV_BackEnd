package com.codevumc.codev_backend.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoLanguageOfStudy {
    private long co_languageId;
    private String co_language;
    private String co_logo;
}
