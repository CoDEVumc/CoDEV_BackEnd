package com.codevumc.codev_backend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoProject {
    private long co_projectId;
    private String co_email;
    private String co_nickname;
    private String co_viewer;
    private String co_title;
    private String co_location;
    private String co_content;
    private String co_mainImg;
    private DevType co_process;
    private String co_deadLine;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private long co_heartCount;
    private int co_total;
    private boolean co_heart;
    private String co_parts;
    private String co_languages;
    private boolean co_recruitStatus;
    private List<CoRecruitOfProject> co_applicantList;
    private List<CoPhotos> co_photos;
    private List<CoPartOfProject> co_partList;
    private List<CoLanguage> co_languageList;

    @Getter
    @AllArgsConstructor
    public enum DevType {
        ING("ING"), TEST("TEST"), FIN("FIN");
        private String value;

        @JsonCreator
        public static DevType from(String s) {
            return DevType.valueOf(s.toUpperCase());
        }
    }

}