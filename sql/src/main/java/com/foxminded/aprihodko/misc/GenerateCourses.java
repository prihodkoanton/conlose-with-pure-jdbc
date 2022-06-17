package com.foxminded.aprihodko.misc;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.model.Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateCourses {

    private final CourseDao courseDao;

    public GenerateCourses(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    private final List<String> nameOfCourse = Arrays.asList("Math", "Music", "Dance", "Chemistry", "Physics", "English",
            "Belarussian", "Ukrainian", "Java", "Spring");
    private final List<String> descriptionOfCourse = Arrays.asList("simple mathematical operations",
            "Learning the basics of music", "Basics of movements", "Chemical reactions of acids",
            "Reactions in the interaction of two bodies", "The use of tenses in speech", "Cases in speech",
            "Slava Ukraine", "Learing Java core", "Framework to Java");

    public void generateDate(Connection connection, int courseCount) throws SQLException {
        if (courseCount > 10) {
            throw new IllegalArgumentException("Count of Course must be less then 11");
        }
        generateCourse(connection, courseCount);
    }

    private List<Course> generateCourse(Connection connection, int count) throws SQLException {
        List<Course> result = new ArrayList<Course>();
        for (int i = 0; i < count; i++) {
            result.add(courseDao.save(connection, new Course(nameOfCourse.get(i), descriptionOfCourse.get(i))));
        }
        return result;
    }
}