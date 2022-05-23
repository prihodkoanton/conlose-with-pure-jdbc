package com.foxminded.aprihodko.dao.impl;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.model.Course;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseDaoImplTest extends DaoTestBaseClass {

    CourseDao dao = new CourseDaoImpl();

    @Test
    void shouldFindById() throws SQLException {
        sql("sql/find_by_id_courses.sql");
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
        sql("sql/find_all_courses.sql");
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
}