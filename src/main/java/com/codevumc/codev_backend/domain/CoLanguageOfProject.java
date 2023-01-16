package com.codevumc.codev_backend.domain;

import lombok.*;
import org.springframework.context.annotation.Scope;

import java.sql.Timestamp;

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
