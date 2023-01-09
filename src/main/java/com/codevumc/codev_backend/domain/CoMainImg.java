package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoMainImg {
    private long co_mainImgId;
    private String co_mainImg;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
}
