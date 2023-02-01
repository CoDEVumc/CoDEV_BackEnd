package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoLanguage;
import com.codevumc.codev_backend.domain.CoLocation;
import com.codevumc.codev_backend.domain.CoPart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoCategoryMapper {
    List<CoLocation> getLocation();
    List<CoPart> getParts();
    List<CoLanguage> getLanguageOfPart(String co_part);
}
