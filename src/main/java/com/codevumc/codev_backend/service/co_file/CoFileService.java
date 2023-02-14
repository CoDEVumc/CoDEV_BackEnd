package com.codevumc.codev_backend.service.co_file;

import com.codevumc.codev_backend.domain.CoPhotos;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CoFileService {

    CoPhotos storeFile(MultipartFile file, String co_targetId, String co_type);
    void deleteFile(String co_targetId, String co_type) ;
    ResponseEntity<Resource> loadFileAsResource(HttpServletRequest request, String fileName);
    ResponseEntity<Resource> showImage(@RequestParam Map<String, String> param);
    String getCo_MainImg(String co_type, String co_targetId);

}
