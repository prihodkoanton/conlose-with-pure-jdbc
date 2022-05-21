CREATE SCHEMA IF NOT EXISTS school;

DROP TABLE IF EXISTS school.courses;
DROP TABLE IF EXISTS school.groups;
DROP TABLE IF EXISTS school.students;

CREATE TABLE school.courses
(
    course_id          bigserial                         NOT NULL,
    course_name        text COLLATE pg_catalog."default" NOT NULL,
    course_description text COLLATE pg_catalog."default" NOT NULL
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
    CONSTRAINT students_pkey PRIMARY KEY (group_id)
);


