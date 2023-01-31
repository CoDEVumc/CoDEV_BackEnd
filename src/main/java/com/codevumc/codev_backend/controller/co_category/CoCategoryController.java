package com.codevumc.codev_backend.controller.co_category;

import com.codevumc.codev_backend.domain.CoLocation;
import com.codevumc.codev_backend.mapper.CoCategoryMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/codev/category")
public class CoCategoryController {
    private final CoCategoryMapper coCategoryMapper;

    public CoCategoryController(CoCategoryMapper coCategoryMapper){
        this.coCategoryMapper = coCategoryMapper;
    }

    @GetMapping("/location")
    public List<CoLocation> getLocation(){
        List<CoLocation> coLocationList = coCategoryMapper.getLocation();
        return coLocationList;
    }
}
