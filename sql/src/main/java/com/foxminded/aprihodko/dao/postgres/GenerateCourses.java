package com.foxminded.aprihodko.dao.postgres;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.model.Course;

public class GenerateCourses {
   
   private final CourseDao courseDao;
   private List<Course> courses;
   private final List<String> nameOfCourse = new ArrayList<>() {{
      add("Math");
      add("Music");
      add("Dance");
      add("Chemistry");
      add("Physics");
      add("English");
      add("Belarussian");
      add("Ukarin");
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
