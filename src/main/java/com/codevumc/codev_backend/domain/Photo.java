package com.codevumc.codev_backend.domain;

import java.sql.Timestamp;

public class Photo {
    private long co_photoId;
    private long co_projectId;
    private String co_uuId;
    private String co_fileName;
    private String co_filePath;
    private String co_fileUrl;
    private String co_fileDownloadPath;
    private long co_fileSize;
    private Timestamp createdAt;
}
