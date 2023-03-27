package com.codevumc.codev_backend.service.co_board_search;

import com.codevumc.codev_backend.domain.CoBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoBoardMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class CoBoardSearchServiceImpl extends ResponseService implements CoBoardSearchService {
    private final CoBoardMapper coBoardMapper;

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }

    @Override
    public CoDevResponse searchBoard(String co_email, int limit, int offset, int pageNum, boolean coMyBoard, String searchTag, String sortingTag, String type) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("searchTag", setting(searchTag));
        condition.put("sortingTag", sortingTag);
        condition.put("coMyBoard", coMyBoard ? co_email : null);
        condition.put("limit", limit);
        condition.put("offset", offset);
        List<CoQnaBoard> objectOfQna;
        List<CoInfoBoard> objectOfInfo;
        try {
            List<CoBoard> coBoardOptional = this.coBoardMapper.searchBoard(condition);

            CoDevResponse result = setResponse(200, "success", coBoardOptional);
            if(type.equals("QNA")) {
                objectOfQna = this.coBoardMapper.searchListQnaBoard(condition);
                addResponse("boards", objectOfQna);
            }
            else if(type.equals("INFO")) {
                objectOfInfo = this.coBoardMapper.searchListInfoBoard(condition);
                addResponse("boards", objectOfInfo);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
