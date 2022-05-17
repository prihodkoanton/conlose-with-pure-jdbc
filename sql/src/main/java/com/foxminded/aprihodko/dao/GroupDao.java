package com.foxminded.aprihodko.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.foxminded.aprihodko.model.Group;

public interface GroupDao extends CrudDao<Group, Long>{
   Optional<Group> findByName(Connection connection, String name) throws SQLException;
}
