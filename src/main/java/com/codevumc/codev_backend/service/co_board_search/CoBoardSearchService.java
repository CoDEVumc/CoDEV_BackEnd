package com.codevumc.codev_backend.service.co_board_search;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoBoardSearchService {
    CoDevResponse searchBoard(String co_email, String searchTag);

}
