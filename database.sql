create schema course_manager_system collate utf8mb4_bin;

create table if not exists administrative_class
(
    class_id       bigint auto_increment comment '班级编号'
        primary key,
    class_name     varchar(50)      null comment '班级名称',
    grade          int default 2023 not null comment '年级',
    major_name     varchar(50)      null comment '专业名称',
    direction_name varchar(50)      null comment '方向名称',
    student_number int default 0    not null comment '学生人数'
)
    comment '行政班级';

create table if not exists course
(
    course_id                    varchar(50)    not null comment '课程编号'
        primary key,
    score                        int default 0  not null comment '学分',
    total_class_hour             int default 0  not null comment '总课时',
    theoretical_class_hour       int default 0  not null comment '理论课时',
    experimental_class_hour      int default 0  not null comment '实验课时',
    week_theoretical_class_hour  int default 0  not null comment '周理论课时',
    week_experimental_class_hour int default 0  not null comment '周实验课时',
    start_week                   int default 1  not null comment '起始周',
    end_week                     int default 16 not null comment '结束周',
    belong_system                varchar(50)    null comment '归属系',
    course_name                  varchar(50)    null comment '课程名称'
)
    comment '课程表';

create table if not exists course_teacher_teach_class_group_teach_type
(
    course_id            varchar(50) not null comment '课程编码',
    teacher_id           varchar(50) not null comment '教师编号',
    teach_class_group_id bigint      not null comment '教学班组编号',
    course_type          int         not null comment '课程类型：0-理论，1-实验',
    primary key (course_id, teacher_id, teach_class_group_id, course_type)
)
    comment '课程教师教学班映射表';

create table if not exists course_volunteer
(
    course_volunteer_id bigint auto_increment comment '课程志愿编号'
        primary key,
    status              tinyint default 2 not null comment '状态：0-失败，1-成功，2-等待',
    volunteer_time      json              null comment '志愿时间',
    school_timetable_id bigint            not null comment '课程表编号'
)
    comment '课程志愿表';

create table if not exists permission
(
    permission_id   bigint auto_increment comment '权限id'
        primary key,
    permission_name varchar(50) null comment '权限名称'
)
    comment '权限表';

create table if not exists role
(
    role_id   bigint auto_increment comment '角色id'
        primary key,
    role_name varchar(50) null comment '角色名称'
)
    comment '角色表';

create table if not exists role_permission
(
    role_id       bigint not null comment '角色id',
    permission_id bigint not null comment '权限id',
    primary key (role_id, permission_id)
)
    comment '角色-权限映射表';

create table if not exists school_timetable
(
    school_timetable_id  bigint auto_increment comment '课程表编号'
        primary key,
    teach_class_group_id bigint        not null comment '教学班级组编号',
    teacher_id           varchar(50)   not null comment '教师编号',
    week_day             int           null comment '周几',
    day_section_start    int           null comment '一天第几节',
    course_id            varchar(50)   not null comment '课程编号',
    day_section_end      int           null,
    course_type          int default 0 not null comment '课程类型：0-理论，1-实验',
    constraint teach_class_group_teacher_course_course_type
        unique (teach_class_group_id, teacher_id, course_id, course_type)
)
    comment '课程表';

create table if not exists teach_class_group
(
    teach_class_group_id bigint auto_increment comment '教学课堂组编号'
        primary key,
    class_id_set         json        not null comment '行政班级编号集合',
    group_name           varchar(50) null comment '教学班级组名称'
)
    comment '教学班级组';

create table if not exists teacher
(
    teacher_id    varchar(50) not null comment '教师编号'
        primary key,
    teacher_name  varchar(50) null comment '教师姓名',
    teacher_level varchar(50) null comment '教师职称'
)
    comment '教师信息表';

create table if not exists user
(
    user_id  varchar(50)  not null comment '用户id'
        primary key,
    username varchar(50)  null comment '用户名',
    nickname varchar(50)  null comment '昵称',
    password varchar(100) null comment '密码'
)
    comment '用户表';

create table if not exists user_role
(
    user_id varchar(50) not null comment '用户id',
    role_id bigint      not null comment '角色id',
    primary key (user_id, role_id)
)
    comment '用户-角色映射表';

