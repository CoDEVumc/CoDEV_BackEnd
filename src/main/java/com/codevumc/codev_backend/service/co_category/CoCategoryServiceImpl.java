package com.codevumc.codev_backend.service.co_category;

import com.codevumc.codev_backend.domain.CoLocation;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoCategoryMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CoCategoryServiceImpl extends ResponseService implements CoCategoryService {
    private final CoCategoryMapper coCategoryMapper;

    @Override
    public CoDevResponse getLocation(){
        try {
            List<CoLocation> coLocationList = this.coCategoryMapper.getLocation();
            return setResponse(200,"message",coLocationList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
