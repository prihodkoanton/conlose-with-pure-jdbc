package com.foxminded.aprihodko.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.foxminded.aprihodko.model.Students;

public interface StudentDao extends CrudDao<Students, Long>{
   Optional<Students> findByName(Connection connection, String name) throws SQLException;
}
