package com.codevumc.codev_backend.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoLanguageOfProject {
    private long co_languageId;
    private String co_language;
    private String co_logo;
}
