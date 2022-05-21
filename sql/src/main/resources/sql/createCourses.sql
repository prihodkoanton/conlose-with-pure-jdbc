--drop table school.courses;
CREATE TABLE IF NOT EXISTS school.courses
(
    course_id bigserial NOT NULL,
    course_name text COLLATE pg_catalog."default" NOT NULL,
    course_description text COLLATE pg_catalog."default" NOT NULL
);