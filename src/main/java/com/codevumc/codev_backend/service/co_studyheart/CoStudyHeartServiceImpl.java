package com.codevumc.codev_backend.service.co_studyheart;

import com.codevumc.codev_backend.domain.CoHeartOfProject;
import com.codevumc.codev_backend.domain.CoHeartOfStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CoStudyHeartServiceImpl extends ResponseService implements CoStudyHeartService {
    private final CoStudyMapper coStudyMapper;

    @Override
    public CoDevResponse changeHeart(String co_email, Long co_studyId) {
        try{

            if(coStudyMapper.getCoHeartOfStudyEmail(co_studyId, co_email) == null) {
                this.coStudyMapper.insertCoHeartOfStudy(co_email,co_studyId);
                return setResponse(200, "Complete", "찜등록이 완료되었습니다.");
            }else{
                this.coStudyMapper.deleteCoHeartOfStudy(co_email,co_studyId);
                return setResponse(200, "Complete", "찜등록이 취소되었습니다.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
