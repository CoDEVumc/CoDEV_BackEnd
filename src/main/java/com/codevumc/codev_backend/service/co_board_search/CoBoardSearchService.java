package com.codevumc.codev_backend.service.co_board_search;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoBoardSearchService {
    CoDevResponse searchBoard(String co_email, int limit, int offset, int pageNum, boolean coMyBoard, String searchTag, String sortingTag, String type);

}
