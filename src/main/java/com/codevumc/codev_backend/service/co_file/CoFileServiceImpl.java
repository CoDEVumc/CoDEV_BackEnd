package com.codevumc.codev_backend.service.co_file;

import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.file.FileDownloadException;
import com.codevumc.codev_backend.file.FileUploadException;
import com.codevumc.codev_backend.file.FileUploadProperties;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CoFileServiceImpl implements CoFileService{
    private final Path fileLocation;
    private final CoPhotosMapper coPhotos;

    @Autowired
    public CoFileServiceImpl(FileUploadProperties fileUploadProperties, CoPhotosMapper coPhotos) {
        this.fileLocation = Paths.get(fileUploadProperties.getUploadDir()).toAbsolutePath(). normalize();
        this.coPhotos = coPhotos;
        try {
            Files.createDirectories(this.fileLocation);
        } catch(Exception e) {
            throw new FileUploadException("File Directory maked failed");
        }
    }

    @Override
    public CoPhotos storeFile(MultipartFile file, String co_targetId, String co_type) {
        if(file != null)
            return uploadFile(file, co_targetId, co_type);
        return null;
    }

    @Override
    public void deleteFile(String co_targetId, String co_type) {
        List<CoPhotos> coPhotos = this.coPhotos.findByCoTargetId(co_targetId, co_type);
        if(coPhotos != null) {
            for(CoPhotos list : coPhotos) {
                File listOfFile = new File(list.getCo_filePath());
                if(listOfFile.exists())
                    listOfFile.delete();
            }
            this.coPhotos.deleteCoPhotoOfProject(co_targetId);
        }
    }

    @Override
    public ResponseEntity<Resource> loadFileAsResource(HttpServletRequest request, String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            String originFileName = getOriginFileName(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                String contentType = getContentType(request, resource);

                if(contentType == null)
                    contentType = "application/octet-stream";

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originFileName + "\"")
                        .body(resource);
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException(fileName + "File Not Found");
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> showImage(Map<String, String> param) {
        Optional<com.codevumc.codev_backend.domain.CoPhotos> coPhotoOfProject = coPhotos.findByCo_uuId(param.get("name"));
        if(coPhotoOfProject.isPresent()) {
            String fileUrl = coPhotoOfProject.get().getCo_filePath().replace("\\", "/");
            System.out.println("fileUrl = " + fileUrl);

            Resource resource = new FileSystemResource(fileUrl);
            if(!resource.exists())
                return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

            HttpHeaders httpHeaders = new HttpHeaders();
            Path filePath = null;
            try {
                filePath = Paths.get(fileUrl);
                httpHeaders.add("Content-Type", Files.probeContentType(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<Resource>(resource, httpHeaders, HttpStatus.OK);
        }

        return null;
    }

    public String getCo_MainImg(String co_type, String co_targetId) {
        Optional<CoPhotos> coPhoto = coPhotos.findByCo_mainImg(co_type, co_targetId);
        if(coPhoto.isPresent())
            return coPhoto.get().getCo_fileUrl();
        return null;
    }


    private CoPhotos uploadFile(MultipartFile file, String co_targetId, String co_type) {
        String originFileName = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath(getUUIDFileName(originFileName));
        try {
            if(fileName.contains(".."))
                throw new FileUploadException("File Name is Not Visible");
            Path targetLocation = Path.of(this.fileLocation.toString() + "/" + co_type + "/" + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String filePath = targetLocation.toString();
            //TO-DO MAKE ENUM
            File FileOfFilePath = new File(filePath.toString());
            long bytes = (FileOfFilePath.length() / 1024);
            String uuidFileName = FileOfFilePath.getName();
            String fileUrl = getFileUrl("/image?name=", uuidFileName);
            String fileDownloadUri = getFileUrl("/downloadFile/", uuidFileName);

            return insertPhoto(co_targetId, co_type, uuidFileName, originFileName, filePath.toString(), fileUrl, fileDownloadUri, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadException("[" + fileName + "] File upload failed");
        }
    }

    private CoPhotos insertPhoto(String co_targetId, String co_type, String uuidFileName, String originFileName, String filePath, String fileUrl, String fileDownloadUri, long bytes) {
        com.codevumc.codev_backend.domain.CoPhotos coPhotos =  com.codevumc.codev_backend.domain.CoPhotos.builder()
                .co_targetId(co_targetId)
                .co_type(co_type)
                .co_uuId(uuidFileName)
                .co_fileName(originFileName)
                .co_filePath(filePath)
                .co_fileUrl(fileUrl)
                .co_fileDownloadPath(fileDownloadUri)
                .co_fileSize(bytes)
                .build();
        this.coPhotos.insertCoPhoto(coPhotos);
        return coPhotos;
    }

    private String getUUIDFileName(String fileName) {
        Date now = new Date();
        String[] name = fileName.split("\\.");
        StringBuilder sb = new StringBuilder(name[0]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uploadTime = sdf.format(now);
        //확장자
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        sb.append(uploadTime);
        sb.append(extension);

        return sb.toString();
    }

    private String getFileUrl(String urlPath, String uuidFileName) {
        StringBuilder sb = new StringBuilder(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());
        sb.append(urlPath);
        sb.append(uuidFileName);
        return sb.toString();
    }

    private String getOriginFileName(String uuId) {
        Optional<CoPhotos> coPhotoOfProject = coPhotos.findByCo_uuId(uuId);
        if(coPhotoOfProject.isPresent())
            return coPhotoOfProject.get().getCo_fileName();
        return null;
    }

    private String getContentType(HttpServletRequest request, Resource resource) {
        try {
            return request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
