package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoBoardMapper {
    List<CoBoard> searchBoard(Map<String, Object> coBoardSearchDto);
    List<CoInfoBoard> searchListInfoBoard(Map<String, Object> coBoardSearchDto);
    List<CoQnaBoard> searchListQnaBoard(Map<String, Object> coBoardSearchDto);
}
