package com.foxminded.aprihodko.dao.impl;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
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
        sql("sql/forGroups/find_by_id_groups.sql");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:7432/antonprihodko",
                "anton", "1234");
        inTransaction(datasource, connections -> dao.deleteById(connections, 1000L));
        Optional<Group> shouldBeEmpty = fromTransaction(datasource, connections -> dao.findById(connections, 1000L));
        assertTrue(shouldBeEmpty.isEmpty());
    }
    
    @Test
    void shouldNotDeleteById() throws SQLException{
        sql("sql/forGroups/find_by_id_groups.sql");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:7432/antonprihodko",
                "anton", "1234");
        Exception e = assertThrows(SQLException.class,
                () -> inTransaction(datasource, connections -> dao.deleteById(connections, 1L)));
        assertEquals("Exception in transaction", e.getMessage());
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
    
    @Test
    void shoudlCreate() throws SQLException{
        Group expected = new Group("newGroup");
        Group actual = fromTransaction(datasource, connection -> dao.save(connection, expected)); 
        assertNotNull(actual.getId());
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }
    
}
