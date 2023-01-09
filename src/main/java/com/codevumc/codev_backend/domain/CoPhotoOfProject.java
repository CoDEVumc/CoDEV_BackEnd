package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
public class CoPhotoOfProject {
    private long co_popId;
    private long co_projectId;
    private String co_uuId;
    private String co_fileName;
    private String co_url;
    private String co_filePath;
    private String fileDownloadPath;
    private Double co_fileSize;
    private Timestamp createAt;

}
