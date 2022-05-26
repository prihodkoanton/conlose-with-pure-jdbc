package com.foxminded.aprihodko.dao.impl;

import com.foxminded.aprihodko.dao.AbstractCrudDao;
import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.mappers.StudentsMapper;
import com.foxminded.aprihodko.model.Students;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentsDaoImpl extends AbstractCrudDao<Students, Long> implements StudentDao {
    public static final String SELECT_ONE = "SELECT * FROM school.students where student_id = ?";
    public static final String FIND_BY_NAME = "SELECT * FROM school.students where first_name = ?";
    public static final String FIND_BY_COURSE_ID = "SELECT s.* from school.students s left join school.student_courses sc on s.student_id = sc.student_ref where sc.course_ref = ?";
    public static final String SELECT_ALL = "SELECT * FROM school.students";
    public static final String INSERT_ONE = "INSERT INTO school.students(group_id, first_name, last_name) VALUES (?, ?, ?)";
    public static final String UPDATE = "UPDATE school.students SET first_name = ?, last_name = ? where student_id = ?";
    public static final String DELETE_ONE = "DELETE FROM school.students WHERE student_id = ?";
    public static final String ASSGIN_COURSE_TO_STUDENT = "INSERT INTO school.student_courses(student_ref, course_ref) VALUES (?, ?)";
    
    private final StudentsMapper mapper;

    public StudentsDaoImpl() {
        this.mapper = new StudentsMapper();
    }

    @Override
    public Optional<Students> findById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ONE)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper.apply(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<Students> findAll(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Students> students = new ArrayList<>();
                while (rs.next()) {
                    students.add(mapper.apply(rs));
                }
                return students;
            }
        }
    }

    @Override
    public void deleteById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_ONE)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to delete student (id = " + id + ")");
            }
        }
    }

    @Override
    public Optional<Students> findByName(Connection connection, String name) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper.apply(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    protected Students create(Connection connection, Students entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_ONE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getGroupID());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to create student " + entity);
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Unable to retrieve id");
                }
                Long id = rs.getLong(1);
                return new Students(id, entity.getFirstName(), entity.getLastName());
            }
        }
    }

    @Override
    protected Students update(Connection connection, Students entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setLong(3, entity.getId());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to update student " + entity);
            }
            return new Students(entity.getId(), entity.getFirstName(), entity.getLastName());
        }
    }

    @Override
    public List<Students> findByCourseId(Connection connection, Long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_COURSE_ID)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to find student by course (id = " + id + ")");
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Students> students = new ArrayList<>();
                while (rs.next()) {
                    students.add(mapper.apply(rs));
                }
                return students;
            }
        }
    }

    @Override
    public void assignCourseToStudent(Connection connection, Long studentID, Long courseID) throws SQLException {
        
        try(PreparedStatement ps = connection.prepareStatement(ASSGIN_COURSE_TO_STUDENT)){
            ps.setLong(1, studentID);
            ps.setLong(2, courseID);
            if(ps.executeUpdate() != 1) {
                throw new SQLException("Unable assign course (id = " + courseID + ")" + " to student (id = " + studentID + ")");
            }
        }
    }

    @Override
    public void removeCourseFromStudent(Connection connection, Long studentID, Long courseID) throws SQLException {
        // TODO Auto-generated method stub
        
    }
    
    private static Long generateRandomStudentID() {
        Long randomId = null;
        return randomId;
    }
    private Long generateRandomCurseID() {
        final Long coutOfCourse = 10L;
        Long randomId = null;
        return randomId;
    }
}
