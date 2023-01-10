package com.codevumc.codev_backend.service.co_file;

import com.codevumc.codev_backend.domain.CoPhotoOfProject;
import com.codevumc.codev_backend.file.FileDownloadException;
import com.codevumc.codev_backend.file.FileUploadException;
import com.codevumc.codev_backend.file.FileUploadProperties;
import com.codevumc.codev_backend.mapper.CoPhotoOfProjectMapper;
import lombok.AllArgsConstructor;
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
public class CoFileServiceImpl extends CoFileService{
    private final Path fileLocation;
    private final CoPhotoOfProjectMapper coPhotoOfProjectMapper;

    @Autowired
    public CoFileServiceImpl(FileUploadProperties fileUploadProperties, CoPhotoOfProjectMapper coPhotoOfProjectMapper) {
        this.fileLocation = Paths.get(fileUploadProperties.getUploadDir()).toAbsolutePath(). normalize();
        this.coPhotoOfProjectMapper = coPhotoOfProjectMapper;
        try {
            Files.createDirectories(this.fileLocation);
        } catch(Exception e) {
            throw new FileUploadException("File Directory maked failed");
        }
    }

    @Override
    public CoPhotoOfProject storeFile(MultipartFile file, long co_projectId) {
        if(file != null)
            return uploadFile(file, co_projectId);
        return null;
    }

    @Override
    public CoPhotoOfProject updateFile(MultipartFile file, long co_projectId) {
        deleteFile(co_projectId);
        if(file != null)
            return uploadFile(file, co_projectId);
        return null;
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
        Optional<CoPhotoOfProject> coPhotoOfProject = coPhotoOfProjectMapper.findByCo_uuId(param.get("name"));
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

    private CoPhotoOfProject uploadFile(MultipartFile file, long co_projectId) {
        String originFileName = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath(getUUIDFileName(originFileName));
        System.out.println("FileName = " + fileName);
        try {
            if(fileName.contains(".."))
                throw new FileUploadException("File Name is Not Visible");
            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String filePath = targetLocation.toString();

            File FileOfFilePath = new File(filePath);
            long bytes = (FileOfFilePath.length() / 1024);
            String uuidFileName = FileOfFilePath.getName();
            String fileUrl = "http://semtle.catholic.ac.kr:8080/image?name="+uuidFileName;
            String fileDownloadUri = getFileDownloadUri("/downloadFile/", uuidFileName);
            CoPhotoOfProject coPhotoOfProject =  CoPhotoOfProject.builder()
                    .co_projectId(co_projectId)
                    .co_uuId(uuidFileName)
                    .co_fileName(originFileName)
                    .co_filePath(filePath)
                    .co_fileUrl(fileUrl)
                    .co_fileDownloadPath(fileDownloadUri)
                    .co_fileSize(bytes)
                    .build();
            coPhotoOfProjectMapper.insertCoPhotoOfProject(coPhotoOfProject);
            return coPhotoOfProject;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadException("[" + fileName + "] File upload failed");
        }
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

    private String getFileDownloadUri(String downloadPath, String uuidFileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(downloadPath)
                .path(uuidFileName)
                .toUriString();
    }

    private String getOriginFileName(String uuId) {
        Optional<CoPhotoOfProject> coPhotoOfProject = coPhotoOfProjectMapper.findByCo_uuId(uuId);
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

    private void deleteFile(long co_projectId) {
        List<CoPhotoOfProject> coPhotoOfProjects = coPhotoOfProjectMapper.findByCoProjectId(co_projectId);

        for(CoPhotoOfProject list : coPhotoOfProjects) {
            File listOfFile = new File(list.getCo_filePath());
            if(listOfFile.exists())
                listOfFile.delete();
        }
        coPhotoOfProjectMapper.deleteCoPhotoOfProject(co_projectId);
    }
}
