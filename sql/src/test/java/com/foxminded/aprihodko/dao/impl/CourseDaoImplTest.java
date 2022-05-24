package com.foxminded.aprihodko.dao.impl;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.model.Course;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseDaoImplTest extends DaoTestBaseClass {

    CourseDao dao = new CourseDaoImpl();

    @Test
    void shouldFindById() throws SQLException {
        sql("sql/forCourse/find_by_id_courses.sql");
        Course expected = new Course(1000L, "find-me", "one description");
        Course actual = fromTransaction(datasource, connection -> dao.findById(connection, 1000L)).orElseThrow();
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotFindById() throws SQLException {
        Optional<Course> course = fromTransaction(datasource, connection -> dao.findById(connection, 1L));
        assertTrue(course.isEmpty());
    }

    @Test
    void shouldFindAll() throws SQLException {
        sql("sql/forCourse/find_all_courses.sql");
        List<Course> expected = Arrays.asList(
                new Course(1000L, "one", "one description"),
                new Course(1001L, "two", "two description"),
                new Course(1002L, "three", "three description"),
                new Course(1003L, "four", "four description")
        );

        List<Course> actual = fromTransaction(datasource, connection ->
                dao.findAll(connection)
        );

        assertEquals(expected, actual);
    }
    
    @Test
    void shouldDeleteById() throws SQLException{
        sql("sql/forCourse/find_by_id_courses.sql");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:7432/antonprihodko",
                "anton", "1234");
        inTransaction(datasource, connections -> dao.deleteById(connections, 1000L));
        Optional<Course> shouldBeEmpty = fromTransaction(datasource, connections -> dao.findById(connections, 1000L));
        assertTrue(shouldBeEmpty.isEmpty());
    }
    
    @Test
    void shouldNotDeleteById() throws SQLException{
        sql("sql/forCourse/find_by_id_courses.sql");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:7432/antonprihodko",
                "anton", "1234");
        Exception e = assertThrows(SQLException.class,
                () -> inTransaction(datasource, connections -> dao.deleteById(connections, 1L)));
        assertEquals("Exception in transaction", e.getMessage());
    }
    
    @Test
    void shouldFindByName() throws SQLException{
        sql("sql/forCourse/find_by_id_courses.sql");
        Course expected = new Course(1000L, "find-me", "one description");
        Course actual = fromTransaction(datasource, connection -> dao.findByName(connection, "find-me")).orElseThrow();
        assertEquals(actual, expected);
    }
    
    @Test
    void shouldNotFindByName() throws SQLException{
        Optional<Course> course = fromTransaction(datasource, connection -> dao.findByName(connection, "emptyName"));
        assertTrue(course.isEmpty());
    }
    
    @Test
    void shouldCreate() throws SQLException{
        Course expected = new Course("new", "course");
        Course actual = fromTransaction(datasource, connection -> dao.save(connection, expected));
        assertNotNull(actual.getId());
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }
    
    
    
    
    
    
    
    
    
    
}