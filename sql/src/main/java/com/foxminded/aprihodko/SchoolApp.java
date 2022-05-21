package com.foxminded.aprihodko;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.dao.datasource.SimpleDatasorce;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.dao.impl.GroupDaoImpl;
import com.foxminded.aprihodko.dao.impl.StudentsDaoImpl;
import com.foxminded.aprihodko.misc.GenerateCourses;
import com.foxminded.aprihodko.misc.GenerateStudents;
import com.foxminded.aprihodko.misc.GeneratorGroups;
import com.foxminded.aprihodko.utils.SqlUtils;

import static com.foxminded.aprihodko.utils.TransactionUtils.transaction;
import static com.foxminded.aprihodko.utils.ResourceUtils.loadPropertiesFromResources;

public class SchoolApp implements Closeable {

   private final Datasource datasource;
//   private final StudentsDaoImpl studentsDao;
//   private final GroupDaoImpl groupDao;
   private final CourseDaoImpl courseDao; // Course works

   public SchoolApp(Datasource datasource) throws SQLException {
      this.datasource = datasource;
      this.courseDao = new CourseDaoImpl();
      SqlUtils.executeSqlScriptFile(datasource, "sql/createCourses.sql");
//      this.studentsDao = new StudentsDaoImpl();
//      SqlUtils.executeSqlScriptFile(datasource, "sql/createStudents.sql");
//      this.groupDao = new GroupDaoImpl();
//      SqlUtils.executeSqlScriptFile(datasource, "sql/createGroups.sql");
   }

   private void run() throws SQLException {
//      transaction(datasource, (connection -> new GeneratorGroups(groupDao).generateDate(connection, 10)));
//      transaction(datasource, (connection -> new GenerateStudents(studentsDao).generateData(connection, 200)));
      transaction(datasource, (connection -> new GenerateCourses(courseDao).generateDate(connection, 10)));
   }

   @Override
   public void close() throws IOException {
      try {
//         SqlUtils.executeSqlScriptFile(datasource, "sql/createStudents.sql");
//         SqlUtils.executeSqlScriptFile(datasource, "sql/createGroups.sql");
         SqlUtils.executeSqlScriptFile(datasource, "sql/createCourses.sql");
      } catch (SQLException e) {
         throw new IOException(e);
      }
   }

   public static void main(String[] args) throws IOException, SQLException {
      Properties databaseProperties = loadPropertiesFromResources("db.properties");
      try (Datasource datasource = new SimpleDatasorce(databaseProperties);
            SchoolApp schoolApp = new SchoolApp(datasource);) {
         schoolApp.run();
      }
   }
}
