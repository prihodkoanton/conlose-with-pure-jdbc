package com.foxminded.aprihodko.dao;

import com.foxminded.aprihodko.model.Students;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StudentDao extends CrudDao<Students, Long> {
    Optional<Students> findByName(Connection connection, String name) throws SQLException;
    List<Students> findByCourseId(Connection connection, Long id) throws SQLException;
    void assignCourseToStudent(Connection connection, Long studentID, Long courseID) throws SQLException;
    void removeTheStudentFromOneHisCourse (Connection connection, Long studentID, Long courseID) throws SQLException;
    List<Students> findAllStudentRelatedToCourseWithGivenName(Connection connection, String name) throws SQLException;
}
