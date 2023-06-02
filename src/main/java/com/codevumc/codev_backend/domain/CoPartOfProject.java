package com.codevumc.codev_backend.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoPartOfProject {
    private String co_part;
    private int co_limit;
}
