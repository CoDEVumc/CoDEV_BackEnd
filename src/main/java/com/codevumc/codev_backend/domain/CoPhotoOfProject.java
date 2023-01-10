package com.codevumc.codev_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoPhotoOfProject extends Photo{
    private long co_popId;
    private long co_projectId;
    private String co_uuId;
    private String co_fileName;
    //private String co_url;
    private String co_filePath;
    private String co_fileUrl;
    private String co_fileDownloadPath;
    private long co_fileSize;
    private Timestamp createdAt;



}
