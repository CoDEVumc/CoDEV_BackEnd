package com.codevumc.codev_backend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoStudy {
    private long co_studyId;
    private String co_email;
    private String co_title;
    private String co_location;
    private String co_content;
    private String co_mainImg;
    private DevType co_process;
    private String co_dealLine;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
    private long co_heartCount;
    private boolean co_heart;
    private String co_part;
    private String co_languages;
    private List<CoPhotos> co_photos;
    private List<CoLanguage> co_languageList;

    @AllArgsConstructor
    public enum DevType {
        ING("ING"), TEST("TEST"), FIN("FIN");
        private String value;

        @JsonCreator
        public static CoStudy.DevType from(String s) {
            return CoStudy.DevType.valueOf(s.toUpperCase());
        }
    }
}
