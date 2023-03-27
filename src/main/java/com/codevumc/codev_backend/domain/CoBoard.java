package com.codevumc.codev_backend.domain;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoBoard {
    private String type;
    private int boardCount;
}
