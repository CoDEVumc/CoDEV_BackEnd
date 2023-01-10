package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class CoProjectServiceImpl extends ResponseService implements CoProjectService {
    private final CoProjectMapper coProjectMapper;

    public CoProject insertProject(CoProject coProject) {
        this.coProjectMapper.insertCoProject(coProject);
        return coProject;
    }

    public CoProject getCoProject(long co_projectId) {
        Optional<CoProject> coProject = this.coProjectMapper.findByCoProjectId(co_projectId);
        if(coProject.isPresent())
            return coProject.get();
        return null;
    }

    public CoDevResponse getCoProject(CoProject coProject) {
        try {
            return setResponse(200, "Complete", coProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
