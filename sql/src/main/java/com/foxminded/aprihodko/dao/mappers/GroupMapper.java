package com.foxminded.aprihodko.dao.mappers;

import com.foxminded.aprihodko.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.foxminded.aprihodko.model.Group.GROUP_ID;
import static com.foxminded.aprihodko.model.Group.GROUP_NAME;

public class GroupMapper implements Mapper<Group> {

    @Override
    public Group apply(ResultSet rSet) throws SQLException {
        return new Group(rSet.getLong(GROUP_ID), rSet.getString(GROUP_NAME));
    }
}
