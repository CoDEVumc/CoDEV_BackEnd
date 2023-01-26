package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.domain.CoApplicantInfo;
import com.codevumc.codev_backend.domain.CoPartOfProject;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CoProjectRecruitServiceImpl extends ResponseService implements CoProjectRecruitService {
    private final CoProjectMapper coProjectMapper;

    @Override
    public CoDevResponse insertCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject){
        try {
            boolean coRecruitOfStatus = coProjectMapper.getCoRecruitStatus(coRecruitOfProject.getCo_email(),coRecruitOfProject.getCo_projectId());
            Optional<CoProject> coProject = coProjectMapper.getCoProject(coRecruitOfProject.getCo_projectId());
            if(coProject.isPresent()) {
                if(coProject.get().getCo_email().equals(coRecruitOfProject.getCo_email()))
                    return setResponse(403, "Forbidden", "작성자는 지원할 수 없습니다..");
                if(!coRecruitOfStatus){
                    this.coProjectMapper.insertCoRecruitOfProject(coRecruitOfProject);
                    return setResponse(200, "message", "지원되었습니다");
                }
                else {
                    return setResponse(445,"message","이미 지원한 프로젝트입니다");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse cancelCoRecruitOfProject(String co_email, long co_projectId) {
        Map<String, Object> recruitDto = new HashMap<>();
        recruitDto.put("co_email", co_email);
        recruitDto.put("co_projectId", co_projectId);
        try {
            boolean coRecruitStatus = coProjectMapper.getCoRecruitStatus(co_email, co_projectId);
            Optional<CoProject> coProject = coProjectMapper.getCoProject(co_projectId);
            if(coProject.isPresent()) {
                if(coProject.get().getCo_email().equals(co_email))
                    return setResponse(403, "Forbidden", "작성자는 지원할 수 없습니다..");
                if (coRecruitStatus) {
                    this.coProjectMapper.cancelCoRecruitOfProject(recruitDto);
                    return setResponse(200, "message", "지원 취소되었습니다.");
                } else {
                    return setResponse(445, "message", "이미 지원이 취소되었습니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getCoApplicantsOfProject(String co_email, long co_projectId) {
        try{
            Optional<CoProject> coProjectOptional = coProjectMapper.getCoProject(co_projectId);
            if(coProjectOptional.isPresent()){  // 글 존재여부
                if (!coProjectOptional.get().getCo_email().equals(co_email)) // pm이 아닌 경우
                    return setResponse(403, "Forbidden", "조회 권한이 없습니다.");
                List<CoApplicantInfo> coApplicantsOfProject = new ArrayList<>();
                List<CoPartOfProject> coPartsOfProject = this.coProjectMapper.getCoPartList(co_projectId);
                Map<String, Object> coPartInfoDto = new HashMap<>();
                // 파트 수 만큼 반복(추후 파트 변동 가능성)
                for (CoPartOfProject coPartOfProject : coPartsOfProject) {
                    coPartInfoDto.put("co_projectId", co_projectId);
                    coPartInfoDto.put("co_part", coPartOfProject.getCo_part());
                    CoApplicantInfo coApplicantInfo = CoApplicantInfo.builder()
                            .co_part(coPartOfProject.getCo_part())
                            .co_limit(coPartOfProject.getCo_limit())
                            .co_applicants(this.coProjectMapper.getCoApplicantsOfProject(coPartInfoDto))
                            .build();
                    coApplicantsOfProject.add(coApplicantInfo);
                }
                return setResponse(200, "message", coApplicantsOfProject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
