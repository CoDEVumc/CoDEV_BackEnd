-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- CoUser Table Create SQL
-- 테이블 생성 SQL - CoUser
CREATE TABLE CoUser
(
    `co_email`     VARCHAR(50)     NOT NULL    COMMENT '이메일', 
    `co_password`  VARCHAR(50)     NOT NULL    COMMENT '비밀번호', 
    `co_nickName`  VARCHAR(20)     NOT NULL    COMMENT '닉네임', 
    `role`         VARCHAR(30)     NOT NULL    DEFAULT 'USER' COMMENT '권한', 
    `profileImg`   VARCHAR(300)    NULL        DEFAULT 'https://avatars.githubusercontent.com/u/74559561?s=80&v=4' COMMENT '프로필 이미지', 
    `createdAt`    TIMESTAMP       NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`    TIMESTAMP       NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`       BIT             NOT NULL    DEFAULT true COMMENT '상태. 활성/비활성/탈퇴', 
     PRIMARY KEY (co_email)
);

-- 테이블 Comment 설정 SQL - CoUser
ALTER TABLE CoUser COMMENT '사용자 정보';


-- CoStudy Table Create SQL
-- 테이블 생성 SQL - CoStudy
CREATE TABLE CoStudy
(
    `co_studyId`  BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '스터디 id', 
    `co_email`    VARCHAR(50)     NOT NULL    COMMENT '이메일', 
    `co_title`    VARCHAR(100)    NOT NULL    COMMENT '제목', 
    `co_content`  TEXT            NOT NULL    COMMENT '내용', 
    `co_logo`     TEXT            NOT NULL    COMMENT '로고 이미지', 
    `co_limit`    INT             NOT NULL    COMMENT '제한 인원', 
    `co_process`  BIT             NOT NULL    DEFAULT true COMMENT '진행 상태. 모집중: true / 모집완료: false', 
    `createdAt`   TIMESTAMP       NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`   TIMESTAMP       NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`      BIT             NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_studyId)
);

-- 테이블 Comment 설정 SQL - CoStudy
ALTER TABLE CoStudy COMMENT '스터디 모집 게시판';

-- Foreign Key 설정 SQL - CoStudy(co_email) -> CoUser(co_email)
ALTER TABLE CoStudy
    ADD CONSTRAINT FK_CoStudy_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoStudy(co_email)
-- ALTER TABLE CoStudy
-- DROP FOREIGN KEY FK_CoStudy_co_email_CoUser_co_email;


-- CoProject Table Create SQL
-- 테이블 생성 SQL - CoProject
CREATE TABLE CoProject
(
    `co_project`  BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '프로젝트 id', 
    `co_email`    VARCHAR(50)     NOT NULL    COMMENT '이메일', 
    `co_title`    VARCHAR(100)    NOT NULL    COMMENT '제목', 
    `co_content`  TEXT            NOT NULL    COMMENT '내용', 
    `co_mainImg`  TEXT            NULL        COMMENT '대표 이미지. 글 대표 이미지 -> 10장 중 랜덤 1장', 
    `co_limit`    INT             NOT NULL    COMMENT '제한 인원', 
    `co_process`  BIT             NOT NULL    DEFAULT true COMMENT '진행 상태. 모집중: true / 모집완료: false', 
    `createdAt`   TIMESTAMP       NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`   TIMESTAMP       NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`      BIT             NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_project)
);

-- 테이블 Comment 설정 SQL - CoProject
ALTER TABLE CoProject COMMENT '프로젝트 모집 게시판';

-- Foreign Key 설정 SQL - CoProject(co_email) -> CoUser(co_email)
ALTER TABLE CoProject
    ADD CONSTRAINT FK_CoProject_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoProject(co_email)
-- ALTER TABLE CoProject
-- DROP FOREIGN KEY FK_CoProject_co_email_CoUser_co_email;


-- CoLanguage Table Create SQL
-- 테이블 생성 SQL - CoLanguage
CREATE TABLE CoLanguage
(
    `co_languageId`  BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '언어 id', 
    `co_language`    VARCHAR(45)    NOT NULL    COMMENT '언어 이름', 
    `co_logo`        VARCHAR(45)    NOT NULL    COMMENT '로고 이미지', 
    `createdAt`      TIMESTAMP      NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`      TIMESTAMP      NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
     PRIMARY KEY (co_languageId)
);

-- 테이블 Comment 설정 SQL - CoLanguage
ALTER TABLE CoLanguage COMMENT '언어 카테고리';


-- CoChat Table Create SQL
-- 테이블 생성 SQL - CoChat
CREATE TABLE CoChat
(
    `co_chatName`   VARCHAR(100)    NOT NULL    COMMENT '채팅방이름', 
    `chatType`      VARCHAR(3)      NOT NULL    COMMENT '타입. 1:1 -> QA  1:N -> DEV', 
    `co_chatFile`   TEXT            NOT NULL    COMMENT '채팅파일로그', 
    `createdAt`      TIMESTAMP       NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `co_chatImage`  text            NOT NULL    COMMENT '채팅방 이미지', 
     PRIMARY KEY (co_chatName)
);


-- CoPart Table Create SQL
-- 테이블 생성 SQL - CoPart
CREATE TABLE CoPart
(
    `co_partId`  BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '파트 id', 
    `co_part`    VARCHAR(45)    NOT NULL    COMMENT '파트 명', 
    `createdAt`  TIMESTAMP      NOT NULL    DEFAULT current_timestamp COMMENT '생성 시간', 
    `updatedAt`  TIMESTAMP      NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
     PRIMARY KEY (co_partId)
);

-- 테이블 Comment 설정 SQL - CoPart
ALTER TABLE CoPart COMMENT '파트 카테고리';


-- CoLanguageOfProject Table Create SQL
-- 테이블 생성 SQL - CoLanguageOfProject
CREATE TABLE CoLanguageOfProject
(
    `co_lopId`       BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_projectId`   BIGINT       NOT NULL    COMMENT '프로젝트 id', 
    `co_languageId`  BIGINT       NOT NULL    COMMENT '언어 id', 
    `createdAt`      TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`      TIMESTAMP    NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`         BIT          NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_lopId)
);

-- 테이블 Comment 설정 SQL - CoLanguageOfProject
ALTER TABLE CoLanguageOfProject COMMENT '프로젝트에서 사용하는 언어 저장 테이블';

-- Foreign Key 설정 SQL - CoLanguageOfProject(co_languageId) -> CoLanguage(co_languageId)
ALTER TABLE CoLanguageOfProject
    ADD CONSTRAINT FK_CoLanguageOfProject_co_languageId_CoLanguage_co_languageId FOREIGN KEY (co_languageId)
        REFERENCES CoLanguage (co_languageId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoLanguageOfProject(co_languageId)
-- ALTER TABLE CoLanguageOfProject
-- DROP FOREIGN KEY FK_CoLanguageOfProject_co_languageId_CoLanguage_co_languageId;

-- Foreign Key 설정 SQL - CoLanguageOfProject(co_projectId) -> CoProject(co_project)
ALTER TABLE CoLanguageOfProject
    ADD CONSTRAINT FK_CoLanguageOfProject_co_projectId_CoProject_co_project FOREIGN KEY (co_projectId)
        REFERENCES CoProject (co_project) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoLanguageOfProject(co_projectId)
-- ALTER TABLE CoLanguageOfProject
-- DROP FOREIGN KEY FK_CoLanguageOfProject_co_projectId_CoProject_co_project;


-- CoLanguageOfUser Table Create SQL
-- 테이블 생성 SQL - CoLanguageOfUser
CREATE TABLE CoLanguageOfUser
(
    `co_louId`       BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_email`       VARCHAR(50)    NOT NULL    COMMENT '이메일', 
    `co_languageId`  BIGINT         NOT NULL    COMMENT '언어 id', 
    `createdAt`      TIMESTAMP      NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`      TIMESTAMP      NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
     PRIMARY KEY (co_louId)
);

-- 테이블 Comment 설정 SQL - CoLanguageOfUser
ALTER TABLE CoLanguageOfUser COMMENT '사용자가 사용하는 언어 설정 -> 삭제 시 delete문 사용할 예정!';

-- Foreign Key 설정 SQL - CoLanguageOfUser(co_languageId) -> CoLanguage(co_languageId)
ALTER TABLE CoLanguageOfUser
    ADD CONSTRAINT FK_CoLanguageOfUser_co_languageId_CoLanguage_co_languageId FOREIGN KEY (co_languageId)
        REFERENCES CoLanguage (co_languageId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoLanguageOfUser(co_languageId)
-- ALTER TABLE CoLanguageOfUser
-- DROP FOREIGN KEY FK_CoLanguageOfUser_co_languageId_CoLanguage_co_languageId;

-- Foreign Key 설정 SQL - CoLanguageOfUser(co_email) -> CoUser(co_email)
ALTER TABLE CoLanguageOfUser
    ADD CONSTRAINT FK_CoLanguageOfUser_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoLanguageOfUser(co_email)
-- ALTER TABLE CoLanguageOfUser
-- DROP FOREIGN KEY FK_CoLanguageOfUser_co_email_CoUser_co_email;


-- CoRecruit Table Create SQL
-- 테이블 생성 SQL - CoRecruit
CREATE TABLE CoRecruit
(
    `co_recruitId`  BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_email`      VARCHAR(50)    NOT NULL    COMMENT '이메일', 
    `co_projectId`  BIGINT         NOT NULL    COMMENT '프로젝트 id', 
    `co_partId`     BIGINT         NOT NULL    COMMENT '파트 id', 
    `isApproved`    BIT            NOT NULL    DEFAULT false COMMENT '승인 여부', 
    `createdAt`     TIMESTAMP      NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`     TIMESTAMP      NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`        BIT            NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_recruitId)
);

-- 테이블 Comment 설정 SQL - CoRecruit
ALTER TABLE CoRecruit COMMENT '사용자가 지원한 프로젝트';

-- Foreign Key 설정 SQL - CoRecruit(co_email) -> CoUser(co_email)
ALTER TABLE CoRecruit
    ADD CONSTRAINT FK_CoRecruit_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoRecruit(co_email)
-- ALTER TABLE CoRecruit
-- DROP FOREIGN KEY FK_CoRecruit_co_email_CoUser_co_email;

-- Foreign Key 설정 SQL - CoRecruit(co_projectId) -> CoProject(co_project)
ALTER TABLE CoRecruit
    ADD CONSTRAINT FK_CoRecruit_co_projectId_CoProject_co_project FOREIGN KEY (co_projectId)
        REFERENCES CoProject (co_project) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoRecruit(co_projectId)
-- ALTER TABLE CoRecruit
-- DROP FOREIGN KEY FK_CoRecruit_co_projectId_CoProject_co_project;


-- CoHeartOfProject Table Create SQL
-- 테이블 생성 SQL - CoHeartOfProject
CREATE TABLE CoHeartOfProject
(
    `co_hopId`      BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_email`      VARCHAR(50)    NOT NULL    COMMENT '이메일', 
    `co_projectId`  BIGINT         NOT NULL    COMMENT '프로젝트 id', 
    `createdAt`     TIMESTAMP      NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`     TIMESTAMP      NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`        BIT            NOT NULL    DEFAULT true COMMENT '상태', 
     PRIMARY KEY (co_hopId)
);

-- 테이블 Comment 설정 SQL - CoHeartOfProject
ALTER TABLE CoHeartOfProject COMMENT '프로젝트 모집글 찜하기';

-- Foreign Key 설정 SQL - CoHeartOfProject(co_email) -> CoUser(co_email)
ALTER TABLE CoHeartOfProject
    ADD CONSTRAINT FK_CoHeartOfProject_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoHeartOfProject(co_email)
-- ALTER TABLE CoHeartOfProject
-- DROP FOREIGN KEY FK_CoHeartOfProject_co_email_CoUser_co_email;

-- Foreign Key 설정 SQL - CoHeartOfProject(co_projectId) -> CoProject(co_project)
ALTER TABLE CoHeartOfProject
    ADD CONSTRAINT FK_CoHeartOfProject_co_projectId_CoProject_co_project FOREIGN KEY (co_projectId)
        REFERENCES CoProject (co_project) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoHeartOfProject(co_projectId)
-- ALTER TABLE CoHeartOfProject
-- DROP FOREIGN KEY FK_CoHeartOfProject_co_projectId_CoProject_co_project;


-- CoChatOfProject Table Create SQL
-- 테이블 생성 SQL - CoChatOfProject
CREATE TABLE CoChatOfProject
(
    `co_email`     VARCHAR(50)     NOT NULL    COMMENT '이메일', 
    `co_chatName`  VARCHAR(100)    NOT NULL    COMMENT '채팅방이름', 
     PRIMARY KEY (co_email, co_chatName)
);

-- Foreign Key 설정 SQL - CoChatOfProject(co_email) -> CoUser(co_email)
ALTER TABLE CoChatOfProject
    ADD CONSTRAINT FK_CoChatOfProject_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoChatOfProject(co_email)
-- ALTER TABLE CoChatOfProject
-- DROP FOREIGN KEY FK_CoChatOfProject_co_email_CoUser_co_email;

-- Foreign Key 설정 SQL - CoChatOfProject(co_chatName) -> CoChat(co_chatName)
ALTER TABLE CoChatOfProject
    ADD CONSTRAINT FK_CoChatOfProject_co_chatName_CoChat_co_chatName FOREIGN KEY (co_chatName)
        REFERENCES CoChat (co_chatName) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoChatOfProject(co_chatName)
-- ALTER TABLE CoChatOfProject
-- DROP FOREIGN KEY FK_CoChatOfProject_co_chatName_CoChat_co_chatName;


-- CoPartOfProject Table Create SQL
-- 테이블 생성 SQL - CoPartOfProject
CREATE TABLE CoPartOfProject
(
    `co_popId`      BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_projectId`  BIGINT       NOT NULL    COMMENT '프로젝트 id', 
    `co_partId`     BIGINT       NOT NULL    COMMENT '파트 id', 
    `createdAt`     TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`     TIMESTAMP    NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`        BIT          NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_popId)
);

-- 테이블 Comment 설정 SQL - CoPartOfProject
ALTER TABLE CoPartOfProject COMMENT '프로젝트에서 사용하는 파트 저장 테이블';

-- Foreign Key 설정 SQL - CoPartOfProject(co_partId) -> CoPart(co_partId)
ALTER TABLE CoPartOfProject
    ADD CONSTRAINT FK_CoPartOfProject_co_partId_CoPart_co_partId FOREIGN KEY (co_partId)
        REFERENCES CoPart (co_partId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoPartOfProject(co_partId)
-- ALTER TABLE CoPartOfProject
-- DROP FOREIGN KEY FK_CoPartOfProject_co_partId_CoPart_co_partId;

-- Foreign Key 설정 SQL - CoPartOfProject(co_projectId) -> CoProject(co_project)
ALTER TABLE CoPartOfProject
    ADD CONSTRAINT FK_CoPartOfProject_co_projectId_CoProject_co_project FOREIGN KEY (co_projectId)
        REFERENCES CoProject (co_project) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoPartOfProject(co_projectId)
-- ALTER TABLE CoPartOfProject
-- DROP FOREIGN KEY FK_CoPartOfProject_co_projectId_CoProject_co_project;


-- CoHeartOfStudy Table Create SQL
-- 테이블 생성 SQL - CoHeartOfStudy
CREATE TABLE CoHeartOfStudy
(
    `co_hosId`    BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_email`    VARCHAR(50)    NOT NULL    COMMENT '이메일', 
    `co_studyId`  BIGINT         NOT NULL    COMMENT '스터디 id', 
    `createdAt`   TIMESTAMP      NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`   TIMESTAMP      NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`      BIT            NULL        DEFAULT true COMMENT '상태', 
     PRIMARY KEY (co_hosId)
);

-- 테이블 Comment 설정 SQL - CoHeartOfStudy
ALTER TABLE CoHeartOfStudy COMMENT '스터디 모집글 찜하기';

-- Foreign Key 설정 SQL - CoHeartOfStudy(co_studyId) -> CoStudy(co_studyId)
ALTER TABLE CoHeartOfStudy
    ADD CONSTRAINT FK_CoHeartOfStudy_co_studyId_CoStudy_co_studyId FOREIGN KEY (co_studyId)
        REFERENCES CoStudy (co_studyId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoHeartOfStudy(co_studyId)
-- ALTER TABLE CoHeartOfStudy
-- DROP FOREIGN KEY FK_CoHeartOfStudy_co_studyId_CoStudy_co_studyId;

-- Foreign Key 설정 SQL - CoHeartOfStudy(co_email) -> CoUser(co_email)
ALTER TABLE CoHeartOfStudy
    ADD CONSTRAINT FK_CoHeartOfStudy_co_email_CoUser_co_email FOREIGN KEY (co_email)
        REFERENCES CoUser (co_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoHeartOfStudy(co_email)
-- ALTER TABLE CoHeartOfStudy
-- DROP FOREIGN KEY FK_CoHeartOfStudy_co_email_CoUser_co_email;


-- CoLanguageOfStudy Table Create SQL
-- 테이블 생성 SQL - CoLanguageOfStudy
CREATE TABLE CoLanguageOfStudy
(
    `co_losId`       BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_studyId`     BIGINT       NOT NULL    COMMENT '스터디 id', 
    `co_languageId`  BIGINT       NOT NULL    COMMENT '언어 id', 
    `createdAt`      TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`      TIMESTAMP    NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`         BIT          NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_losId)
);

-- 테이블 Comment 설정 SQL - CoLanguageOfStudy
ALTER TABLE CoLanguageOfStudy COMMENT '스터디에서 사용하는 언어 저장 테이블';

-- Foreign Key 설정 SQL - CoLanguageOfStudy(co_studyId) -> CoStudy(co_studyId)
ALTER TABLE CoLanguageOfStudy
    ADD CONSTRAINT FK_CoLanguageOfStudy_co_studyId_CoStudy_co_studyId FOREIGN KEY (co_studyId)
        REFERENCES CoStudy (co_studyId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoLanguageOfStudy(co_studyId)
-- ALTER TABLE CoLanguageOfStudy
-- DROP FOREIGN KEY FK_CoLanguageOfStudy_co_studyId_CoStudy_co_studyId;

-- Foreign Key 설정 SQL - CoLanguageOfStudy(co_languageId) -> CoLanguage(co_languageId)
ALTER TABLE CoLanguageOfStudy
    ADD CONSTRAINT FK_CoLanguageOfStudy_co_languageId_CoLanguage_co_languageId FOREIGN KEY (co_languageId)
        REFERENCES CoLanguage (co_languageId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoLanguageOfStudy(co_languageId)
-- ALTER TABLE CoLanguageOfStudy
-- DROP FOREIGN KEY FK_CoLanguageOfStudy_co_languageId_CoLanguage_co_languageId;


-- CoPartOfStudy Table Create SQL
-- 테이블 생성 SQL - CoPartOfStudy
CREATE TABLE CoPartOfStudy
(
    `co_posId`    BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '중간 테이블 id', 
    `co_studyId`  BIGINT       NOT NULL    COMMENT '스터디 id', 
    `co_partId`   BIGINT       NOT NULL    COMMENT '파트 id', 
    `createdAt`   TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성시간', 
    `updatedAt`   TIMESTAMP    NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`      BIT          NOT NULL    DEFAULT true COMMENT '상태. 정상: true / 삭제: false', 
     PRIMARY KEY (co_posId)
);

-- 테이블 Comment 설정 SQL - CoPartOfStudy
ALTER TABLE CoPartOfStudy COMMENT '스터디에서 사용하는 파트 저장 테이블';

-- Foreign Key 설정 SQL - CoPartOfStudy(co_studyId) -> CoStudy(co_studyId)
ALTER TABLE CoPartOfStudy
    ADD CONSTRAINT FK_CoPartOfStudy_co_studyId_CoStudy_co_studyId FOREIGN KEY (co_studyId)
        REFERENCES CoStudy (co_studyId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoPartOfStudy(co_studyId)
-- ALTER TABLE CoPartOfStudy
-- DROP FOREIGN KEY FK_CoPartOfStudy_co_studyId_CoStudy_co_studyId;

-- Foreign Key 설정 SQL - CoPartOfStudy(co_partId) -> CoPart(co_partId)
ALTER TABLE CoPartOfStudy
    ADD CONSTRAINT FK_CoPartOfStudy_co_partId_CoPart_co_partId FOREIGN KEY (co_partId)
        REFERENCES CoPart (co_partId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoPartOfStudy(co_partId)
-- ALTER TABLE CoPartOfStudy
-- DROP FOREIGN KEY FK_CoPartOfStudy_co_partId_CoPart_co_partId;


-- CoMainImg Table Create SQL
-- 테이블 생성 SQL - CoMainImg
CREATE TABLE CoMainImg
(
    `co_mainImgId`  BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '대표 이미지 id', 
    `co_mainImg`    TEXT         NOT NULL    COMMENT '대표 이미지', 
    `createdAt`     TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성 시간', 
    `updatedAt`     TIMESTAMP    NOT NULL    DEFAULT current_timestamp on update current_timestamp COMMENT '업데이트 시간', 
    `status`        BIT          NOT NULL    DEFAULT true COMMENT '상태', 
     PRIMARY KEY (co_mainImgId)
);


-- CoPhotoOfProject Table Create SQL
-- 테이블 생성 SQL - CoPhotoOfProject
CREATE TABLE CoPhotoOfProject
(
    `co_popId`             BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '사진 id', 
    `co_project`           BIGINT       NOT NULL    COMMENT '프로젝트 id', 
    `co_uuId`              TEXT         NOT NULL    COMMENT 'uuid', 
    `co_fileName`          TEXT         NOT NULL    COMMENT '파일명', 
    `co_url`               TEXT         NOT NULL    COMMENT '사진 url', 
    `co_filePath`          TEXT         NOT NULL    COMMENT '파일 경로', 
    `co_fileDownloadPath`  TEXT         NOT NULL    COMMENT '파일 다운로드 경로', 
    `co_fileSize`          DOUBLE       NOT NULL    COMMENT '파일 크기', 
    `createdAt`             TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성 시간', 
     PRIMARY KEY (co_popId)
);

-- 테이블 Comment 설정 SQL - CoPhotoOfProject
ALTER TABLE CoPhotoOfProject COMMENT '프로젝트 사진';

-- Foreign Key 설정 SQL - CoPhotoOfProject(co_project) -> CoProject(co_project)
ALTER TABLE CoPhotoOfProject
    ADD CONSTRAINT FK_CoPhotoOfProject_co_project_CoProject_co_project FOREIGN KEY (co_project)
        REFERENCES CoProject (co_project) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoPhotoOfProject(co_project)
-- ALTER TABLE CoPhotoOfProject
-- DROP FOREIGN KEY FK_CoPhotoOfProject_co_project_CoProject_co_project;


-- CoPhotoOfStudy Table Create SQL
-- 테이블 생성 SQL - CoPhotoOfStudy
CREATE TABLE CoPhotoOfStudy
(
    `co_popId`             BIGINT       NOT NULL    AUTO_INCREMENT COMMENT '사진 id', 
    `co_study`             BIGINT       NOT NULL    COMMENT '스터디 id', 
    `co_uuId`              TEXT         NOT NULL    COMMENT 'uuid', 
    `co_fileName`          TEXT         NOT NULL    COMMENT '파일명', 
    `co_url`               TEXT         NOT NULL    COMMENT '사진 url', 
    `co_filePath`          TEXT         NOT NULL    COMMENT '파일 경로', 
    `co_fileDownloadPath`  TEXT         NOT NULL    COMMENT '파일 다운로드 경로', 
    `co_fileSize`          DOUBLE       NOT NULL    COMMENT '파일 크기', 
    `createdAt`             TIMESTAMP    NOT NULL    DEFAULT current_timestamp COMMENT '생성 시간', 
     PRIMARY KEY (co_popId)
);

-- 테이블 Comment 설정 SQL - CoPhotoOfStudy
ALTER TABLE CoPhotoOfStudy COMMENT '스터디 사진';

-- Foreign Key 설정 SQL - CoPhotoOfStudy(co_study) -> CoStudy(co_studyId)
ALTER TABLE CoPhotoOfStudy
    ADD CONSTRAINT FK_CoPhotoOfStudy_co_study_CoStudy_co_studyId FOREIGN KEY (co_study)
        REFERENCES CoStudy (co_studyId) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - CoPhotoOfStudy(co_study)
-- ALTER TABLE CoPhotoOfStudy
-- DROP FOREIGN KEY FK_CoPhotoOfStudy_co_study_CoStudy_co_studyId;


CREATE TABLE `refreshtoken` (
  `refreshTokenId` int(11) NOT NULL AUTO_INCREMENT,
  `refreshToken` varchar(300) DEFAULT NULL,
  `keyId` varchar(150) DEFAULT NULL,
  `userAgent` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`refreshTokenId`)
);


