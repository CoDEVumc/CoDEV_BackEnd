package com.codevumc.codev_backend.controller.co_category;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.service.co_category.CoCategoryService;
import com.codevumc.codev_backend.service.co_category.CoCategoryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codev/category")
public class CoCategoryController {
    private final CoCategoryService coCategoryService;

    public CoCategoryController(CoCategoryServiceImpl coCategoryService){
        this.coCategoryService = coCategoryService;
    }

    @GetMapping("/location")
    public CoDevResponse getLocation() throws Exception {
        return coCategoryService.getLocation();
    }

    @GetMapping("/parts")
    public CoDevResponse getParts() throws Exception {
        return coCategoryService.getParts();
    }

    @GetMapping("/language")
    public CoDevResponse getLanguageOfPart(@RequestParam("coPart") String co_part) throws Exception {
        return coCategoryService.getLanguageOfPart(co_part);
    }
}
