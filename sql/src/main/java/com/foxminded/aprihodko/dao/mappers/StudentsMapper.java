package com.foxminded.aprihodko.dao.mappers;

import com.foxminded.aprihodko.model.Students;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.foxminded.aprihodko.model.Students.*;

;

public class StudentsMapper implements Mapper<Students> {

    @Override
    public Students apply(ResultSet rSet) throws SQLException {
        return new Students(rSet.getLong(STUDENT_ID), rSet.getInt(GROUP_ID), rSet.getString(FIRRST_NAME), rSet.getString(LAST_NAME));
    }
}
