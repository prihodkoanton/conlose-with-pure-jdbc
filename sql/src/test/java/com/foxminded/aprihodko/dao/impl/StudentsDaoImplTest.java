package com.foxminded.aprihodko.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

import org.junit.jupiter.api.Test;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.model.Students;

class StudentsDaoImplTest extends DaoTestBaseClass{

    StudentDao dao = new StudentsDaoImpl(); 
    
    @Test
    void shouldFindById() throws SQLException{
        sql("sql/forStudents/find_by_id_students.sql");
        Students expected = new Students(1000L, 1000, "find-me", "in-junit-test");
        Students actual = fromTransaction(datasource, connection -> dao.findById(connection, 1000L)).orElseThrow();
        assertEquals(actual, expected);
    }
    
    @Test
    void shouldNotFindById() throws SQLException{
        Optional<Students> student = fromTransaction(datasource, connection -> dao.findById(connection, 1L));
        assertTrue(student.isEmpty());
    }
    
    @Test
    void shouldFindAll() throws SQLException{
        sql("sql/forStudents/find_all_students.sql");
        List<Students> expected = Arrays.asList(
                new Students(1000L, 1000, "one", "one last_name"),
                new Students(1001L, 1001, "two", "two last_name"),
                new Students(1002L, 1002, "three", "three last_name"),
                new Students(1003L, 1003, "four", "four last_name")
                );
        List<Students> actual = fromTransaction(datasource, connection -> dao.findAll(connection));
        assertEquals(actual, expected);
    }
    
    @Test
    void shouldDeleteById() throws SQLException{
        sql("sql/forStudents/find_by_id_students.sql");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:7432/antonprihodko",
                "anton", "1234");
        inTransaction(datasource, connections -> dao.deleteById(connections, 1000L));
        Optional<Students> shouldBeEmpty = fromTransaction(datasource, connections -> dao.findById(connections, 1000L));
        assertTrue(shouldBeEmpty.isEmpty());
    }
    
    @Test
    void shouldNotDeleteById() throws SQLException{
        sql("sql/forStudents/find_by_id_students.sql");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:7432/antonprihodko",
                "anton", "1234");
        Exception e = assertThrows(SQLException.class,
                () -> inTransaction(datasource, connections -> dao.deleteById(connections, 1L)));
        assertEquals("Exception in transaction", e.getMessage());
    }
    
    @Test
    void shouldByName()  throws SQLException{
        sql("sql/forStudents/find_by_id_students.sql");
        Students expected = new Students(1000L, 1000, "find-me", "in-junit-test");
        Students actual = fromTransaction(datasource, connection -> dao.findByName(connection, "find-me")).orElseThrow();
        assertEquals(actual, expected);
    }
    
    @Test
    void shouldNotByName()  throws SQLException{
        sql("sql/forStudents/find_by_id_students.sql");
        Optional<Students> actual = fromTransaction(datasource, connection -> dao.findByName(connection, "emptyName"));
        assertTrue(actual.isEmpty());
    }
    
    
}
