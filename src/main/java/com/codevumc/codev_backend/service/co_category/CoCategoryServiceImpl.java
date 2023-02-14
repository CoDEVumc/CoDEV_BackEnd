package com.codevumc.codev_backend.service.co_category;

import com.codevumc.codev_backend.domain.CoLanguage;
import com.codevumc.codev_backend.domain.CoLocation;
import com.codevumc.codev_backend.domain.CoPart;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoCategoryMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
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

    @Override
    public CoDevResponse getParts(){
        try {
            List<CoPart> coPartList = this.coCategoryMapper.getParts();
            return setResponse(200,"message",coPartList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getLanguageOfPart(String co_part){
        try {
            List<CoLanguage> coLanguageList = this.coCategoryMapper.getLanguageOfPart(co_part);
            return setResponse(200,"message",coLanguageList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
