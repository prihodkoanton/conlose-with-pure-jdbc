package com.foxminded.aprihodko.dao.postgres;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.model.Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateCourses {

    private final CourseDao courseDao;
    private List<Course> courses;
    private final List<String> nameOfCourse = Arrays.asList(
            "Math",
            "Music",
            "Dance",
            "Chemistry",
            "Physics",
            "English",
            "Belarussian",
            "Ukarin",
            "Java",
            "Spring"
    );
    private final List<String> descriptionOfCourse = Arrays.asList(
            "simple mathematical operations",
            "Learning the basics of music",
            "Basics of movements",
            "Chemical reactions of acids",
            "Reactions in the interaction of two bodies",
            "The use of tenses in speech",
            "Cases in speech",
            "Slava Ukraine",
            "Learing Java core",
            "Framework to Java"
    );

    public GenerateCourses(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void generateDate(Connection connection, int groupCount) throws SQLException {
        generateDate(connection, groupCount);
    }

    private List<Course> generateGroups(Connection connection, int count) throws SQLException {
        List<Course> result = new ArrayList<Course>();
        for (int i = 0; i < count; i++) {
            result.add(courseDao.save(connection, new Course(nameOfCourse.get(i), descriptionOfCourse.get(i))));
        }
        return result;
    }
}
