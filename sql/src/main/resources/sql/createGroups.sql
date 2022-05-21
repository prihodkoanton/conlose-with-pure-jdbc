--drop table if exists school.groups;

CREATE TABLE IF NOT EXISTS school.groups
(
   group_id bigserial NOT NULL,
   group_name text COLLATE pg_catalog."default"  NOT NULL,
   CONSTRAINT groups_pkey PRIMARY KEY (group_id)
)