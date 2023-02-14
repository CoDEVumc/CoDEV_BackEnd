package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.CoApplicantCount;
import com.codevumc.codev_backend.domain.CoApplicantInfo;
import com.codevumc.codev_backend.domain.CoApplicantsInfoOfStudy;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
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
class CoStudyRecruitServiceImplTest {

    @Mock
    CoStudyMapper coStudyMapper;

    @Test
    @DisplayName("스터디 지원자 조회 테스트")
    public void GET_ALL_STUDY_APPLICANTS() throws Exception {
        // given
        long co_studyId = 1;
        String co_part = "백엔드";
        Map<String, Object> coStudyDto = new HashMap<>();
        coStudyDto.put("co_studyId", co_studyId);
        coStudyDto.put("co_part", co_part.toUpperCase());

        int co_tempSavedApplicantsCount = 1;

        CoApplicantCount coApplicantCount = CoApplicantCount.builder()
                .co_part("백엔드")
                .co_limit(5)
                .co_applicantsCount(10)
                .build();

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
        when(coStudyMapper.getTempsavedApplicantsCount(co_studyId)).thenReturn(co_tempSavedApplicantsCount);
        when(coStudyMapper.getCoApplicantsCount(co_studyId)).thenReturn(coApplicantCount);
        when(coStudyMapper.getCoApplicantsInfo(coStudyDto)).thenReturn(coApplicantsInfo);

        CoApplicantsInfoOfStudy coApplicantsInfoOfStudy = CoApplicantsInfoOfStudy.builder()
                .co_part(co_part.toUpperCase())
                .co_tempSavedApplicantsCount(this.coStudyMapper.getTempsavedApplicantsCount(co_studyId))
                .co_applicantCount(this.coStudyMapper.getCoApplicantsCount(co_studyId))
                .co_applicantsInfo(this.coStudyMapper.getCoApplicantsInfo(coStudyDto))
                .build();

        // then
        assertAll(
                () -> assertEquals("백엔드", coApplicantsInfoOfStudy.getCo_part()),

                () -> assertEquals(1, coApplicantsInfoOfStudy.getCo_tempSavedApplicantsCount()),

                () -> assertEquals("백엔드", coApplicantsInfoOfStudy.getCo_applicantCount().getCo_part()),
                () -> assertEquals(5, coApplicantsInfoOfStudy.getCo_applicantCount().getCo_limit()),
                () -> assertEquals(10, coApplicantsInfoOfStudy.getCo_applicantCount().getCo_applicantsCount()),

                () -> assertEquals(1, coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getCo_portfolioId()),
                () -> assertEquals("simhani1@naver.com", coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getCo_email()),
                () -> assertEquals("title", coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getCo_title()),
                () -> assertEquals("test", coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getCo_name()),
                () -> assertEquals("testURL", coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getProfileImg()),
                () -> assertEquals("백엔드", coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getCo_part()),
                () -> assertEquals(false, coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).isCo_temporaryStorage()),
                () -> assertEquals("2023-01-31", coApplicantsInfoOfStudy.getCo_applicantsInfo().get(0).getCreatedAt())
        );
    }
}