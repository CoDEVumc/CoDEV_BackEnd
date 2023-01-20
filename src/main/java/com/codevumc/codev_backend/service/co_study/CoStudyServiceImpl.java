package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class CoStudyServiceImpl extends ResponseService implements CoStudyService {
    private final CoStudyMapper coStudyMapper;
    private final CoPhotosMapper coPhotosMapper;

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }

    @Override
    public CoDevResponse getCoStudies(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("co_email", co_email);
        condition.put("co_locationTag", co_locationTag);
        condition.put("co_partTag", setting(co_partTag));
        condition.put("co_keyword", setting(co_keyword));
        condition.put("co_processTag", co_processTag);
        List<CoStudy> coStudies = this.coStudyMapper.getCoStudies(condition);
        try {
            return setResponse(200, "success", coStudies);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
