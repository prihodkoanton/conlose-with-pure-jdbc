package com.foxminded.aprihodko.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T apply(ResultSet rSet) throws SQLException;
}
