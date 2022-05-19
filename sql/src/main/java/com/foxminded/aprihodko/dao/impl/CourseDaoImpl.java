package com.foxminded.aprihodko.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.foxminded.aprihodko.dao.AbstractCrudDao;
import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.mappers.CourseMapper;
import com.foxminded.aprihodko.model.Course;

public class CourseDaoImpl extends AbstractCrudDao<Course, Long> implements CourseDao{
   public static final String SELECT_ONE = "SELECT * FROM school.courses where course_id = ?";
   public static final String FIND_BY_NAME = "SELECT * FROM school.courses where course_name = ?";
   public static final String SELECT_ALL = "SELECT * FROM school.courses";
   public static final String INSERT_ONE = "INSERT INTO school.courses(course_name, course_description) VALUES (?, ?)";
   public static final String UPDATE = "UPDATE school.courses SET course_name = ?, courses_description = ? where course_id = ?";
   public static final String DELETE_ONE = "DELETE FROM school.courses WHERE course_id = ?";
   
   private CourseMapper mapper;
   
   public CourseDaoImpl() {
      this.mapper = new CourseMapper();
   }

   @Override
   public Optional<Course> findById(Connection connection, Long id) throws SQLException {
      try(PreparedStatement ps = connection.prepareStatement(SELECT_ONE)){
         ps.setLong(1, id);
         try(ResultSet rs = ps.executeQuery()){
            if(rs.next()) {
               return Optional.of(mapper.apply(rs));
            } else {
               return Optional.empty();
            }
         }
      }
   }

   @Override
   public List<Course> findAll(Connection connection) throws SQLException {
      try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL)){
         try(ResultSet rs = ps.executeQuery()){
            List<Course> courses = new ArrayList<>();
            while(rs.next()) {
               courses.add(mapper.apply(rs));
            }
            return courses;
         }
      }
   }

   @Override
   public void deleteById(Connection connection, Long id) throws SQLException {
      try(PreparedStatement ps = connection.prepareStatement(DELETE_ONE)){
         ps.setLong(1, id);
         if (ps.executeUpdate() != 1) {
            throw new SQLException("Unable to delete course (id = " + id + ")");
         }
      }
   }

   @Override
   public Optional<Course> fingByName(Connection connection, String name) throws SQLException {
      try(PreparedStatement ps = connection.prepareStatement(FIND_BY_NAME)){
         ps.setString(1, name);
         try(ResultSet rs = ps.executeQuery()){
            if (rs.next()) {
               return Optional.of(mapper.apply(rs));
            } else {
               return Optional.empty();
            }
         }
      }
   }

   @Override
   protected Course create(Connection connection, Course entity) throws SQLException {
      try(PreparedStatement ps = connection.prepareStatement(INSERT_ONE, Statement.RETURN_GENERATED_KEYS)){
         ps.setString(1, entity.getName());
         ps.setString(2, entity.getDescription());
         if (ps.executeUpdate() != 1) {
            throw new SQLException("Unable to create course " + entity);
         }
         try(ResultSet rs = ps.getGeneratedKeys()){
            if (!rs.next()) {
               throw new SQLException("Unable to retrieve id");
            }
            Long id = rs.getLong(1);
            return new Course(id, entity.getName(), entity.getDescription());
         }
      }
   }

   @Override
   protected Course update(Connection connection, Course entity) throws SQLException {
      try(PreparedStatement ps = connection.prepareStatement(UPDATE)){
         ps.setString(1, entity.getName());
         ps.setString(2, entity.getDescription());
         ps.setLong(3, entity.getId());
         if (ps.executeUpdate() != 1) {
            throw new SQLException("Unable to update course " + entity);
         } 
         return new Course(entity.getName(), entity.getDescription());
      }
   }
}
