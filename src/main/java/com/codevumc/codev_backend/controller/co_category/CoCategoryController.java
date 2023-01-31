package com.codevumc.codev_backend.controller.co_category;

import com.codevumc.codev_backend.domain.CoLocation;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.service.co_category.CoCategoryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/codev/category")
public class CoCategoryController {
    private final CoCategoryServiceImpl coCategoryService;

    public CoCategoryController(CoCategoryServiceImpl coCategoryService){
        this.coCategoryService = coCategoryService;
    }

    @GetMapping("/location")
    public CoDevResponse getLocation() throws Exception {
        return coCategoryService.getLocation();
    }
}
