package com.foxminded.aprihodko.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.foxminded.aprihodko.model.Course;
import static com.foxminded.aprihodko.model.Course.COURSE_ID;
import static com.foxminded.aprihodko.model.Course.COURSE_NAME;

public class CourseMapper implements Mapper<Course>{

   @Override
   public Course apply(ResultSet rSet) throws SQLException {
      return new Course(rSet.getLong(COURSE_ID), rSet.getString(COURSE_NAME));
   }

}
