package com.codevumc.codev_backend.service.co_category;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoCategoryService {
    CoDevResponse getLocation();
    CoDevResponse getParts();
    CoDevResponse getLanguageOfPart(String co_part);
}
