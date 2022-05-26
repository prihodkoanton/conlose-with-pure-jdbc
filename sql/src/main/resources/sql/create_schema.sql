CREATE SCHEMA IF NOT EXISTS school;

DROP TABLE IF EXISTS school.courses;
DROP TABLE IF EXISTS school.groups;
DROP TABLE IF EXISTS school.students;

CREATE TABLE school.courses
(
    course_id          bigserial                         NOT NULL,
    course_name        text COLLATE pg_catalog."default" NOT NULL,
    course_description text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT course_pkey PRIMARY KEY (course_id)
);

CREATE TABLE school.groups
(
    group_id   bigserial                         NOT NULL,
    group_name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
);

CREATE TABLE school.students
(
    student_id bigserial                         NOT NULL,
    group_id   bigserial                         NOT NULL,
    first_name text COLLATE pg_catalog."default" NOT NULL,
    last_name  text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (student_id)
);

CREATE TABLE school.student_courses
(
    student_ref bigint not null references school.students (student_id),
    course_ref  bigint not null references school.courses (course_id),
    unique (student_ref, course_ref)
);

