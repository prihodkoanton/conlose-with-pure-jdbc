package com.foxminded.aprihodko.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.foxminded.aprihodko.model.Students;
import static com.foxminded.aprihodko.model.Students.STUDENT_ID;
import static com.foxminded.aprihodko.model.Students.FIRRST_NAME;
import static com.foxminded.aprihodko.model.Students.LAST_NAME;;

public class StudentsMapper implements Mapper<Students>{

   @Override
   public Students apply(ResultSet rSet) throws SQLException {
      return new Students(rSet.getLong(STUDENT_ID), rSet.getString(FIRRST_NAME), rSet.getString(LAST_NAME));
   }
}
