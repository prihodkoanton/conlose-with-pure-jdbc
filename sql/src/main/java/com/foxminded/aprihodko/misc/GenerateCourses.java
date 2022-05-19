package com.foxminded.aprihodko.misc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.model.Course;

public class GenerateCourses {
   
   private final CourseDao courseDao;
   
   public GenerateCourses(CourseDao courseDao) {
      this.courseDao = courseDao;
   }
   private final List<String> nameOfCourse = new ArrayList<>() {{
      add("Math");
      add("Music");
      add("Dance");
      add("Chemistry");
      add("Physics");
      add("English");
      add("Belarussian");
      add("Ukrainian");
      add("Java");
      add("Spring");
   }};
   private final List<String> descriptionOfCourse = new ArrayList<>() {{
      add("simple mathematical operations");
      add("Learning the basics of music");
      add("Basics of movements");
      add("Chemical reactions of acids");
      add("Reactions in the interaction of two bodies");
      add("The use of tenses in speech");
      add("Cases in speech");
      add("Slava Ukraine");
      add("Learing Java core");
      add("Framework to Java");
   }};
   
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