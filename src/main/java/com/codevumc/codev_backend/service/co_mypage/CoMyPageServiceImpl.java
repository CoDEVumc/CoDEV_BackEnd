package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoMyPageServiceImpl extends ResponseService implements CoMyPageService{
    private final CoMyPageMapper coMyPageMapper;
}
