package com.foxminded.aprihodko.misc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.model.Course;

public class StudentCoursesMappingGenerator {
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public StudentCoursesMappingGenerator(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    public void mapStudentsToCourses(Connection connection, int minCourseCount, int maxCourseCount)
            throws SQLException {
        Random rnd = new Random();
        List<Course> courses = courseDao.findAll(connection);
        if (courses.size() < minCourseCount) {
            throw new IllegalArgumentException(
                    "The number of courses '" + courses.size() + "' is less than minCourseCount = " + minCourseCount);
        }
        List<Long> courseId = courses.stream().map(it -> it.getId()).collect(Collectors.toList());
        studentDao.findAll(connection).forEach(student -> {
            Collections.shuffle(courseId);
            int count = minCourseCount + rnd.nextInt(maxCourseCount - minCourseCount);
            for (int i = 0; i < count; i++) {
                try {
                    studentDao.assignCourseToStudent(connection, student.getId(), courseId.get(i));
                } catch (SQLException e) {
                    throw new RuntimeException("Exception during mapping student " + student, e);
                }
            }
        });
    }
}
