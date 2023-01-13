package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPhotos {
    private long co_photoId;
    private long co_targetId;
    private String co_type;
    private String co_uuId;
    private String co_fileName;
    private String co_filePath;
    private String co_fileUrl;
    private String co_fileDownloadPath;
    private long co_fileSize;
    private Timestamp createdAt;



}
