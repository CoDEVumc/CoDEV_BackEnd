package com.codevumc.codev_backend.service.co_board_search;

import com.codevumc.codev_backend.domain.CoBoard;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoBoardMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CoBoardSearchServiceImpl extends ResponseService implements CoBoardSearchService {
    private final CoBoardMapper coBoardMapper;

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }

    @Override
    public CoDevResponse searchBoard(String co_email, String searchTag) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("searchTag", setting(searchTag));
        try {
            List<CoBoard> coBoardOptional = this.coBoardMapper.searchBoard(condition);
            return setResponse(200, "success", coBoardOptional);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
