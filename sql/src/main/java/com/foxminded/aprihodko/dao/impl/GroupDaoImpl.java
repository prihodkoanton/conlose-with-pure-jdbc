package com.foxminded.aprihodko.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.foxminded.aprihodko.dao.AbstractCrudDao;
import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.mappers.GroupMapper;
import com.foxminded.aprihodko.model.Group;

public class GroupDaoImpl extends AbstractCrudDao<Group, Long> implements GroupDao {
    public static final String SELECT_ONE = "SELECT * FROM school.groups where group_id = ?";
    public static final String FIND_BY_NAME = "SELECT * FROM school.groups where group_name = ?";
    public static final String SELECT_ALL = "SELECT * FROM school.groups";
    public static final String INSERT_ONE = "INSERT INTO school.groups(group_name) VALUES (?)";
    public static final String UPDATE = "UPDATE school.groups SET group_name = ? where group_id = ?";
    public static final String DELETE_ONE = "DELETE FROM school.groups WHERE group_id = ?";
    public static final String FIND_ALL_GROUPS_WITH_LESS_OR_EQUALS_STUDENT_COUNT = "SELECT g.group_id, g.group_name, COUNT(s.student_id) AS count_students \n"
            + "FROM school.groups g LEFT OUTER JOIN school.students s ON s.group_id = g.group_id \n"
            + "GROUP BY g.group_id, g.group_name HAVING COUNT(s.student_id) <= ? ORDER BY count_students DESC";

    private final GroupMapper mapper;

    public GroupDaoImpl() {
        this.mapper = new GroupMapper();
    }

    @Override
    public Optional<Group> findById(Connection connection, Long id) throws SQLException {
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
    public List<Group> findAll(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Group> group = new ArrayList<Group>();
                while (rs.next()) {
                    group.add(mapper.apply(rs));
                }
                return group;
            }
        }
    }

    @Override
    public void deleteById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_ONE)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to delete group (id = " + id + ")");
            }
        }
    }

    @Override
    protected Group create(Connection connection, Group entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_ONE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to create group " + entity);
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Unable to retrieve id");
                }
                Long id = rs.getLong(1);
                return new Group(id, entity.getName());
            }
        }
    }

    @Override
    protected Group update(Connection connection, Group entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getName());
            ps.setLong(2, entity.getId());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Unable to update group " + entity);
            }
            return new Group(entity.getId(), entity.getName());
        }
    }

    @Override
    public Optional<Group> findByName(Connection connection, String name) throws SQLException {
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
    public List<Group> findAllGroupsWithLessOrEqualsStudentCount(Connection connection, int countOfStudents)
            throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(FIND_ALL_GROUPS_WITH_LESS_OR_EQUALS_STUDENT_COUNT)) {
            ps.setInt(1, countOfStudents);
            try (ResultSet rs = ps.executeQuery()) {
                List<Group> groups = new ArrayList<>();
                while (rs.next()) {
                    groups.add(mapper.apply(rs));
                }
                return groups;
            }
        }
    }
}
