create table if not exists ChatRoom
(
    roomId      varchar(150)                        not null,
    room_type   varchar(50)                         not null,
    room_title  varchar(100)                        null,
    mainImg     mediumtext                          null,
    createdDate timestamp default CURRENT_TIMESTAMP not null,
    primary key (roomId, room_type)
);

create table if not exists ChatRoomOfUser
(
    roomId    varchar(150)                        not null,
    co_email  varchar(50)                         not null,
    status    bit       default b'0'              not null,
    isRead    int       default 0                 not null,
    updatedAt timestamp default CURRENT_TIMESTAMP not null,
    primary key (roomId, co_email),
    constraint ChatRoomOfUser_ibfk_1
        foreign key (roomId) references ChatRoom (roomId)
);

create table if not exists CoChat
(
    co_chatName  varchar(100)                        not null comment '채팅방이름'
        primary key,
    chatType     varchar(3)                          not null comment '타입. 1:1 -> QA  1:N -> DEV',
    co_chatFile  text                                not null comment '채팅파일로그',
    createdAt    timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    co_chatImage text                                not null comment '채팅방 이미지'
)
    charset = utf8;

create table if not exists CoLocation
(
    co_location varchar(50)                         not null comment '지역명'
        primary key,
    status      bit       default b'1'              not null comment '상태',
    createdAt   timestamp default CURRENT_TIMESTAMP not null comment '생성 시간',
    updatedAt   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간'
)
    comment '지역 카테고리' charset = utf8;

create table if not exists CoMainImg
(
    co_mainImgId bigint auto_increment comment '대표 이미지 id'
        primary key,
    co_mainImg   text                                not null comment '대표 이미지',
    createdAt    timestamp default CURRENT_TIMESTAMP not null comment '생성 시간',
    updatedAt    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status       bit       default b'1'              not null comment '상태'
)
    charset = utf8;

create table if not exists CoPart
(
    co_part   varchar(45)                         not null comment '파트 명'
        primary key,
    createdAt timestamp default CURRENT_TIMESTAMP not null comment '생성 시간',
    updatedAt timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간'
)
    comment '파트 카테고리' charset = utf8;

create table if not exists CoLanguage
(
    co_languageId bigint auto_increment comment '언어 id'
        primary key,
    co_part       varchar(45)                         not null comment '파트 명',
    co_language   varchar(45)                         not null comment '언어 이름',
    co_logo       text                                not null comment '로고 이미지',
    createdAt     timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    constraint FK_CoLanguage_co_part_CoPart_co_part
        foreign key (co_part) references CoPart (co_part)
)
    comment '언어 카테고리' charset = utf8;

create table if not exists CoPhotos
(
    co_photoId          bigint auto_increment comment '사진 id'
        primary key,
    co_targetId         varchar(100)                        not null comment '타겟 id',
    co_type             varchar(20)                         not null comment '타입',
    co_uuId             text                                not null comment 'uuid',
    co_fileName         text                                not null comment '파일명',
    co_filePath         text                                not null comment '파일 경로',
    co_fileUrl          text                                not null comment '사진 url',
    co_fileDownloadPath text                                not null comment '파일 다운로드 경로',
    co_fileSize         double                              not null comment '파일 크기',
    createdAt           timestamp default CURRENT_TIMESTAMP not null comment '생성 시간'
)
    comment '사진 저장 테이블' charset = utf8;

create table if not exists CoUser
(
    co_email     varchar(50)                                                                                         not null comment '이메일'
        primary key,
    co_password  mediumtext                                                                                          not null comment '비밀번호',
    co_nickName  varchar(20)                                                                                         not null comment '닉네임',
    co_name      varchar(30)                                                                                         not null comment '이름',
    co_birth     varchar(45)  default '20202020'                                                                     not null comment '나이',
    co_gender    varchar(45)  default 'temp'                                                                         not null comment '성볇',
    role         varchar(30)  default 'USER'                                                                         not null comment '권한',
    profileImg   varchar(300) default 'http://semtle.catholic.ac.kr:8080/image?name=Profile_Basic20230130012110.png' null comment '프로필 이미지',
    co_loginType varchar(50)                                                                                         not null,
    createdAt    timestamp    default CURRENT_TIMESTAMP                                                              not null comment '생성시간',
    status       bit          default b'1'                                                                           not null comment '상태. 활성/비활성/탈퇴',
    updatedAt    timestamp    default CURRENT_TIMESTAMP                                                              not null on update CURRENT_TIMESTAMP comment '업데이트 시간'
)
    comment '사용자 정보';

create table if not exists CoLanguageOfUser
(
    co_louId      bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_email      varchar(50)                         not null comment '이메일',
    co_languageId bigint                              not null comment '언어 id',
    createdAt     timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    constraint FK_CoLanguageOfUser_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email),
    constraint FK_CoLanguageOfUser_co_languageId_CoLanguage_co_languageId
        foreign key (co_languageId) references CoLanguage (co_languageId)
)
    comment '사용자가 사용하는 언어 설정 -> 삭제 시 delete문 사용할 예정!' charset = utf8;

create table if not exists CoPortfolio
(
    co_portfolioId  bigint auto_increment comment '포트폴리오 id'
        primary key,
    co_email        varchar(50)                         not null comment '이메일',
    co_title        varchar(100)                        not null comment '제목',
    co_rank         varchar(20)                         not null comment '개발 능력',
    co_headLine     varchar(60)                         not null comment '나를 표현하는 한마디',
    co_introduction text                                not null comment '자기소개',
    createdAt       timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt       timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status          bit       default b'1'              not null comment '상태',
    constraint FK_CoPortfolio_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email)
)
    comment '포트폴리오' charset = utf8;

create table if not exists CoLanguageOfPortfolio
(
    co_lopfId      bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_portfolioId bigint                              not null,
    co_languageId  bigint                              not null comment '언어 id',
    createdAt      timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status         bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint CoLanguageOfPortfolio_CoPortfolio_co_portfolioId_fk
        foreign key (co_portfolioId) references CoPortfolio (co_portfolioId)
            on delete cascade,
    constraint FK_CoLanguageOfPotfolio_co_languageId_CoLanguage_co_languageId
        foreign key (co_languageId) references CoLanguage (co_languageId)
)
    comment '프로젝트에서 사용하는 언어 저장 테이블' charset = utf8;

create table if not exists CoLinkOfPortfolio
(
    co_lopId       bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_portfolioId bigint                              not null comment '포트폴리오 id',
    co_link        text                                not null comment '링크',
    createdAt      timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    constraint FK_CoLinkOfPortfolio_co_portfolioId_CoPortfolio_co_portfolioId
        foreign key (co_portfolioId) references CoPortfolio (co_portfolioId)
            on delete cascade
)
    comment '깃허브, 외부 링크' charset = utf8;

create definer = codev@`%` trigger DELETE_LANGUAGES_WHEN_PORTFOLIO_UPDATED
    after update
    on CoPortfolio
    for each row
begin
    delete from CoLanguageOfPortfolio where CoLanguageOfPortfolio.co_portfolioId = OLD.co_portfolioId;
end;

create definer = codev@`%` trigger DELETE_LINKS_WHEN_PORTFOLIO_UPDATED
    after update
    on CoPortfolio
    for each row
begin
    delete from CoLinkOfPortfolio  where co_portfolioId = OLD.co_portfolioId;
end;

create table if not exists CoProject
(
    co_projectId bigint auto_increment comment '프로젝트 id',
    co_email     varchar(50)                           not null comment '이메일',
    co_title     varchar(100)                          not null comment '제목',
    co_location  varchar(50)                           not null comment '지역명',
    co_content   mediumtext                            not null comment '내용',
    co_mainImg   mediumtext                            null comment '대표 이미지. 글 대표 이미지 -> 10장 중 랜덤 1장',
    co_process   varchar(10) default 'ING'             not null comment '진행 상태. 모집중: ING / 심사기간: TEST / 종료: FIN',
    co_deadLine  timestamp                             null comment '마감 기한',
    createdAt    timestamp   default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt    timestamp   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status       bit         default b'1'              not null comment '상태. 정상: true / 삭제: false',
    primary key (co_projectId, co_location),
    constraint FK_CoProject_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email),
    constraint FK_CoProject_co_location_CoLocation_co_location
        foreign key (co_location) references CoLocation (co_location)
)
    comment '프로젝트 모집 게시판';

create table if not exists CoHeartOfProject
(
    co_hopId     bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_email     varchar(50)                         not null comment '이메일',
    co_projectId bigint                              not null comment '프로젝트 id',
    createdAt    timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status       bit       default b'1'              not null comment '상태',
    constraint FK_CoHeartOfProject_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email)
            on delete cascade,
    constraint FK_CoHeartOfProject_co_projectId_CoProject_co_projectId
        foreign key (co_projectId) references CoProject (co_projectId)
            on update cascade on delete cascade
)
    comment '프로젝트 모집글 찜하기' charset = utf8;

create table if not exists CoLanguageOfProject
(
    co_lopId      bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_projectId  bigint                              not null comment '프로젝트 id',
    co_languageId bigint                              not null comment '언어 id',
    createdAt     timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status        bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoLanguageOfProject_co_languageId_CoLanguage_co_languageId
        foreign key (co_languageId) references CoLanguage (co_languageId),
    constraint FK_CoLanguageOfProject_co_projectId_CoProject_co_projectId
        foreign key (co_projectId) references CoProject (co_projectId)
            on delete cascade
)
    comment '프로젝트에서 사용하는 언어 저장 테이블' charset = utf8;

create table if not exists CoPartOfProject
(
    co_popId     bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_projectId bigint                              not null comment '프로젝트 id',
    co_part      varchar(45)                         not null comment '파트 명',
    co_limit     int                                 not null comment '파트별 인원 수',
    createdAt    timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status       bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoPartOfProject_co_part_CoPart_co_part
        foreign key (co_part) references CoPart (co_part),
    constraint FK_CoPartOfProject_co_projectId_CoProject_co_projectId
        foreign key (co_projectId) references CoProject (co_projectId)
            on delete cascade
)
    comment '프로젝트에서 사용하는 파트 저장 테이블' charset = utf8;

create definer = darren@`%` trigger INSERT_RANDOM_IMG
    before insert
    on CoProject
    for each row
BEGIN
    IF new.co_mainImg IS NULL THEN
        SET new.co_mainImg = (SELECT co_mainImg FROM CoMainImg Order By rand() LIMIT 1);
    END IF;
END;

create definer = darren@`%` trigger UPDATE_RANDOM_IMG
    before update
    on CoProject
    for each row
BEGIN
    IF new.co_mainImg IS NULL THEN
        SET new.co_mainImg = (SELECT co_mainImg FROM CoMainImg Order By rand() LIMIT 1);
    END IF;
    IF (select roomId from ChatRoom where roomId = (select concat('OTM_PROJECT_', new.co_projectId))) IS NOT NULL THEN
        update ChatRoom set mainImg = new.co_mainImg WHERE roomId = (select concat('OTM_PROJECT_', new.co_projectId));
    END IF;
END;

create definer = codev@`%` trigger delete_project_photo
    after delete
    on CoProject
    for each row
begin
    delete from CoPhotos where CoPhotos.co_targetId = CAST(OLD.co_projectId as char(100)) and co_type = 'PROJECT';
end;

create table if not exists CoRecruitOfProject
(
    co_ropId            bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_email            varchar(50)                         not null comment '이메일',
    co_projectId        bigint                              not null comment '프로젝트 id',
    co_portfolioId      bigint                              null comment '포트폴리오 id',
    co_partId           varchar(45)                         not null comment '파트 id',
    co_motivation       text                                not null,
    co_temporaryStorage bit       default b'0'              not null,
    isApproved          bit       default b'0'              not null comment '승인 여부',
    createdAt           timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt           timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status              bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoRecruitOfProject_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email),
    constraint FK_CoRecruitOfProject_co_projectId_CoProject_co_projectId
        foreign key (co_projectId) references CoProject (co_projectId)
            on delete cascade
)
    comment '지원/참여한 프로젝트' charset = utf8;

create index FK_CoRecruitOfProject_co_portfolioId_CoPortfolio_co_portfolioId
    on CoRecruitOfProject (co_portfolioId);

create table if not exists CoStudy
(
    co_studyId  bigint auto_increment comment '스터디 id'
        primary key,
    co_email    varchar(50)                           not null comment '이메일',
    co_title    varchar(100)                          not null comment '제목',
    co_location varchar(50)                           not null comment '지역명',
    co_content  mediumtext                            not null comment '내용',
    co_mainImg  mediumtext                            null comment '대표 이미지. 글 대표 이미지 -> 10장 중 랜덤 1장',
    co_deadLine timestamp                             null comment '마감 기한',
    co_process  varchar(10) default 'ING'             not null comment '진행 상태. 모집중: ING / 심사기간: TEST / 종료: FIN',
    createdAt   timestamp   default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt   timestamp   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status      bit         default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoStudy_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email),
    constraint FK_CoStudy_co_location_CoLocation_co_location
        foreign key (co_location) references CoLocation (co_location)
)
    comment '스터디 모집 게시판';

create table if not exists CoHeartOfStudy
(
    co_hosId   bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_email   varchar(50)                         not null comment '이메일',
    co_studyId bigint                              not null comment '스터디 id',
    createdAt  timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status     bit       default b'1'              not null comment '상태',
    constraint FK_CoHeartOfStudy_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email)
            on delete cascade,
    constraint FK_CoHeartOfStudy_co_studyId_CoStudy_co_studyId
        foreign key (co_studyId) references CoStudy (co_studyId)
            on delete cascade
)
    comment '스터디 모집글 찜하기' charset = utf8;

create table if not exists CoLanguageOfStudy
(
    co_losId      bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_studyId    bigint                              not null comment '스터디 id',
    co_languageId bigint                              not null comment '언어 id',
    createdAt     timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status        bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoLanguageOfStudy_co_languageId_CoLanguage_co_languageId
        foreign key (co_languageId) references CoLanguage (co_languageId),
    constraint FK_CoLanguageOfStudy_co_studyId_CoStudy_co_studyId
        foreign key (co_studyId) references CoStudy (co_studyId)
            on delete cascade
)
    comment '스터디에서 사용하는 언어 저장 테이블' charset = utf8;

create table if not exists CoPartOfStudy
(
    co_posId   bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_studyId bigint                              not null comment '스터디 id',
    co_part    varchar(45)                         not null comment '파트 명',
    co_total   int                                 not null comment '파트별 인원 수',
    createdAt  timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status     bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoPartOfStudy_co_part_CoPart_co_part
        foreign key (co_part) references CoPart (co_part),
    constraint FK_CoPartOfStudy_co_studyId_CoStudy_co_studyId
        foreign key (co_studyId) references CoStudy (co_studyId)
            on delete cascade
)
    comment '스터디에서 사용하는 파트 저장 테이블' charset = utf8;

create table if not exists CoRecruitOfStudy
(
    co_rosId            bigint auto_increment comment '중간 테이블 id'
        primary key,
    co_email            varchar(50)                         not null comment '이메일',
    co_studyId          bigint                              not null comment '스터디 id',
    co_portfolioId      bigint                              null comment '포트폴리오 id',
    co_motivation       text                                not null,
    co_temporaryStorage bit       default b'0'              not null,
    isApproved          bit       default b'0'              not null comment '승인 여부',
    createdAt           timestamp default CURRENT_TIMESTAMP not null comment '생성시간',
    updatedAt           timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '업데이트 시간',
    status              bit       default b'1'              not null comment '상태. 정상: true / 삭제: false',
    constraint FK_CoRecruitOfStudy_co_email_CoUser_co_email
        foreign key (co_email) references CoUser (co_email),
    constraint FK_CoRecruitOfStudy_co_portfolioId_CoPortfolio_co_portfolioId
        foreign key (co_portfolioId) references CoPortfolio (co_portfolioId)
            on update cascade on delete set null,
    constraint FK_CoRecruitOfStudy_co_studyId_CoStudy_co_studyId
        foreign key (co_studyId) references CoStudy (co_studyId)
            on delete cascade
)
    comment '지원/참여한 스터디' charset = utf8;

create definer = codev@`%` trigger INSERT_RANDOM_IMG_OF_STUDY
    before insert
    on CoStudy
    for each row
BEGIN
    IF new.co_mainImg IS NULL THEN
        SET new.co_mainImg = (SELECT co_mainImg FROM CoMainImg Order By rand() LIMIT 1);
    END IF;
END;

create definer = codev@`%` trigger UPDATE_RANDOM_IMG_OF_STUDY
    before update
    on CoStudy
    for each row
BEGIN
    IF new.co_mainImg IS NULL THEN
        SET new.co_mainImg = (SELECT co_mainImg FROM CoMainImg Order By rand() LIMIT 1);
    END IF;
    IF (select roomId from ChatRoom where roomId = (select concat('OTM_STUDY_', new.co_studyId))) IS NOT NULL THEN
        update ChatRoom set mainImg = new.co_mainImg WHERE roomId = (select concat('OTM_STUDY_', new.co_studyId));
    END IF;
END;

create definer = codev@`%` trigger delete_study_photo
    after delete
    on CoStudy
    for each row
begin
    delete from CoPhotos where CoPhotos.co_targetId = CAST(OLD.co_studyId as char(100)) and co_type = 'STUDY';
end;

create definer = darren@`%` trigger INSERT_DEFAULT_PROFILE_IMG
    before insert
    on CoUser
    for each row
BEGIN
    IF new.profileImg IS NULL THEN
        SET new.profileImg = 'http://semtle.catholic.ac.kr:8080/image?name=Profile_Basic20230130012110.png';
    END IF;
END;

create definer = darren@`%` trigger UPDATE_DEFAULT_PROFILE_IMG
    before update
    on CoUser
    for each row
BEGIN
    IF new.profileImg IS NULL THEN
        SET new.profileImg = 'http://semtle.catholic.ac.kr:8080/image?name=Profile_Basic20230130012110.png';
    END IF;
END;

create table if not exists refreshtoken
(
    refreshTokenId bigint auto_increment comment '리프레쉬 토큰 id'
        primary key,
    refreshToken   text         not null comment '리프레쉬 토큰',
    keyId          varchar(150) not null comment '이메일',
    userAgent      varchar(300) not null comment 'userAgent'
)
    comment '리프레쉬 토큰' charset = utf8;

create definer = codev@`%` event if not exists CHANGE_PROCESS_OF_PROJECT on schedule
    every '1' DAY
        starts '2023-02-02 00:00:00'
    enable
    do
    update CoProject
    set co_process='TEST'
    where TIMESTAMPDIFF(second, now(), co_deadLine) <= -86400
      and co_process = 'ING';

create definer = codev@`%` event if not exists CHANGE_PROCESS_OF_STUDY on schedule
    every '1' DAY
        starts '2023-02-02 00:00:00'
    enable
    do
    update CoStudy
    set co_process='TEST'
    where TIMESTAMPDIFF(second, now(), co_deadLine) <= -86400
      and co_process = 'ING';



