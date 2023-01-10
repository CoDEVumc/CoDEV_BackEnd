package com.codevumc.codev_backend.controller.co_file;


import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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


}
