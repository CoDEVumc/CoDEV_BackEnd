package com.codevumc.codev_backend.controller.co_file;


import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class CoFileController {

    private final CoFileServiceImpl coFileService;

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, @PathVariable String fileName) {
        return coFileService.loadFileAsResource(request, fileName);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> getImage(@RequestParam Map<String, String> param) {
        return coFileService.showImage(param);
    }

    @PostMapping(value = "/upload", consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void uploadFile(@RequestPart(required = false) MultipartFile[] files) throws Exception {
        List<CoPhotos> coPhotos = Arrays.asList(files)
                .stream()
                .map(file -> coFileService.storeFile(file, "ADMIN", "LANGUAGE"))
                .collect(Collectors.toList());
    }


}
