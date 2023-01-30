package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.domain.CoApplicantInfo;
import com.codevumc.codev_backend.domain.CoApplicantsInfoOfProject;
import com.codevumc.codev_backend.domain.CoPartOfProject;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoProjectRecruitServiceImplTest {

    @Mock
    CoProjectMapper coProjectMapper;

    @Test
    @DisplayName("프로젝트 지원자 조회 테스트")
    public void GET_ALL_PROJECT_APPLICANTS() throws Exception {
        // given
        long co_projectId = 1;
        String co_part = "백엔드";
        Map<String, Object> coProjectDto = new HashMap<>();
        coProjectDto.put("co_projectId", co_projectId);
        coProjectDto.put("co_partId", co_part);

        List<CoPartOfProject> co_recruitmentStatus =  new ArrayList<>();
        co_recruitmentStatus.add(new CoPartOfProject("백엔드", 1));

        List<CoApplicantInfo> coApplicantsInfo = new ArrayList<>();
        CoApplicantInfo coApplicantInfo = CoApplicantInfo.builder()
                .co_portfolioId(1)
                .co_email("simhani1@naver.com")
                .co_title("title")
                .co_name("test")
                .profileImg("testURL")
                .co_part("백엔드")
                .co_temporaryStorage(false)
                .createdAt("2023-01-31")
                .build();
        coApplicantsInfo.add(coApplicantInfo);

        // when
        when(coProjectMapper.getCoPartList(co_projectId)).thenReturn(co_recruitmentStatus);
        when(coProjectMapper.getCoApplicantsInfo(coProjectDto)).thenReturn(coApplicantsInfo);

        CoApplicantsInfoOfProject coApplicantsInfoOfProject = CoApplicantsInfoOfProject.builder()
                .co_recruitmentStatus(this.coProjectMapper.getCoPartList(co_projectId))
                .co_part(co_part)
                .co_appllicantsInfo(this.coProjectMapper.getCoApplicantsInfo(coProjectDto))
                .build();

        // then
        assertAll(
                () -> assertEquals("백엔드", coApplicantsInfoOfProject.getCo_recruitmentStatus().get(0).getCo_part()),
                () -> assertEquals(1, coApplicantsInfoOfProject.getCo_recruitmentStatus().get(0).getCo_limit()),
                () -> assertEquals("백엔드", coApplicantsInfoOfProject.getCo_part()),
                () -> assertEquals(1, coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getCo_portfolioId()),
                () -> assertEquals("simhani1@naver.com", coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getCo_email()),
                () -> assertEquals( "title", coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getCo_title()),
                () -> assertEquals( "test", coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getCo_name()),
                () -> assertEquals( "testURL", coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getProfileImg()),
                () -> assertEquals("백엔드", coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getCo_part()),
                () -> assertEquals(false, coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).isCo_temporaryStorage()),
                () -> assertEquals("2023-01-31", coApplicantsInfoOfProject.getCo_appllicantsInfo().get(0).getCreatedAt())
        );

    }
}