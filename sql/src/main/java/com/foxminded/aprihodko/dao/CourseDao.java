package com.foxminded.aprihodko.dao;

import com.foxminded.aprihodko.model.Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseDao extends CrudDao<Course, Long> {
    Optional<Course> findByName(Connection connection, String name) throws SQLException;
    List<Course> findByStudentId(Connection connection, Long id)throws SQLException;
    
}