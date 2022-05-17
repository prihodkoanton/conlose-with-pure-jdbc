package com.foxminded.aprihodko.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.foxminded.aprihodko.model.Course;

public interface CourseDao extends CrudDao<Course, Long>{
   Optional<Course> fingByName(Connection connection, String name) throws SQLException;
}
