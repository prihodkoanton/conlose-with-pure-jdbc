package com.foxminded.aprihodko.dao.impl;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.model.Group;

class GroupDaoImplTest extends DaoTestBaseClass {

    GroupDao dao = new GroupDaoImpl();

    @Test
    void shoudFindById() throws SQLException {
        sql("sql/forGroups/find_by_id_groups.sql");
        Group expected = new Group(1000L, "find-me");
        Group actual = fromTransaction(datasource, connection -> dao.findById(connection, 1000L)).orElseThrow();
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotFindById() throws SQLException {
        Optional<Group> group = fromTransaction(datasource, connection -> dao.findById(connection, 1L));
        assertTrue(group.isEmpty());
    }

    @Test
    void shouldFindAll() throws SQLException {
        sql("sql/forGroups/find_all_groups.sql");
        List<Group> expected = Arrays.asList(
                new Group(1000L, "one"),
                new Group(1001L, "two"),
                new Group(1002L, "three"),
                new Group(1003L, "four")
                );
        List<Group> actual = fromTransaction(datasource, connection -> dao.findAll(connection));
        assertEquals(actual, expected);
    }
    
    @Test
    void shouldDeleteById() throws SQLException{
        
    }
    
    @Test
    void shouldNotDeleteById() throws SQLException{
        
    }
    
    @Test
    void shouldFindByName() throws SQLException{
        sql("sql/forGroups/find_by_id_groups.sql");
        Group expected = new Group(1000L, "find-me");
        Group actual = fromTransaction(datasource, connection -> dao.findByName(connection, "find-me")).orElseThrow();
        assertEquals(actual, expected);
    }
    
    @Test
    void shouldNotFindByName() throws SQLException{
        Optional<Group> group = fromTransaction(datasource, connection -> dao.findByName(connection, "emptyName"));
        assertTrue(group.isEmpty());
    }
    
}
