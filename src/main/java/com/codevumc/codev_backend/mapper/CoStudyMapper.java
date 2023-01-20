package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoStudy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CoStudyMapper {
    List<CoStudy> getCoStudies(Map<String, Object> condition);
}
