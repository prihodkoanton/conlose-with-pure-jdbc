package com.foxminded.aprihodko.dao;

import com.foxminded.aprihodko.model.Students;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface StudentDao extends CrudDao<Students, Long> {
    Optional<Students> findByName(Connection connection, String name) throws SQLException;
}
