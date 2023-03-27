package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoBoardMapper {
    List<CoBoard> searchBoard(Map<String, Object> coBoardSearchDto);

}
