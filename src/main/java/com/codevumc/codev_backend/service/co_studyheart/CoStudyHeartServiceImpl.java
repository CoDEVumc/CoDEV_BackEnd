package com.codevumc.codev_backend.service.co_studyheart;

import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoStudyHeartServiceImpl extends ResponseService implements CoStudyHeartService {
    private final CoStudyMapper coStudyMapper;
}
