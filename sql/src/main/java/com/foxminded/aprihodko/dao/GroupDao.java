package com.foxminded.aprihodko.dao;

import com.foxminded.aprihodko.model.Group;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GroupDao extends CrudDao<Group, Long> {
    Optional<Group> findByName(Connection connection, String name) throws SQLException;

    List<Group> findAllGroupsWithLessOrEqualsStudentCount(Connection connection, int countOfStudents)
            throws SQLException;
}
