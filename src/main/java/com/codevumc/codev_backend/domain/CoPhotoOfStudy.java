package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoPhotoOfStudy {
    private long co_popId;
    private long co_studyId;
    private String co_uuId;
    private String fileName;
    private String co_filePath;
    private String co_fileDownloadPath;
    private Double co_fileSize;
    private Timestamp createAt;
}
