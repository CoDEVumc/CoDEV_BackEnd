package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPhotoOfStudy {
    private long co_posId;
    private long co_studyId;
    private String co_uuId;
    private String fileName;
    private String co_filePath;
    private String co_fileDownloadPath;
    private long co_fileSize;
    private Timestamp createdAt;
}
