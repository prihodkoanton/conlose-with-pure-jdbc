package com.foxminded.aprihodko.dao.mappers;

import com.foxminded.aprihodko.model.Course;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.foxminded.aprihodko.model.Course.*;

public class CourseMapper implements Mapper<Course> {

    @Override
    public Course apply(ResultSet rSet) throws SQLException {
        return new Course(rSet.getLong(COURSE_ID), rSet.getString(COURSE_NAME), rSet.getString(COURSE_DESCRIPTION));
    }

}
