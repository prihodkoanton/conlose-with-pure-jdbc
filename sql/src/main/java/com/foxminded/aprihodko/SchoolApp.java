package com.foxminded.aprihodko;

import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.dao.datasource.SimpleDatasorce;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.misc.GenerateCourses;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.utils.SqlUtils;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static com.foxminded.aprihodko.utils.ResourceUtils.loadPropertiesFromResources;
import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

public class SchoolApp implements Closeable {

    private final Datasource datasource;
    //   private final StudentsDaoImpl studentsDao;
    private final CourseDaoImpl courseDao;

    public SchoolApp(Datasource datasource) throws SQLException {
        this.datasource = datasource;
        SqlUtils.executeSqlScriptFile(datasource, "sql/create_schema.sql");
        this.courseDao = new CourseDaoImpl();
    }

    private void run() throws SQLException {
        inTransaction(datasource, (connection -> new GenerateCourses(courseDao).generateDate(connection, 10)));
        List<Course> courses = fromTransaction(datasource, connection -> courseDao.findAll(connection));
        System.out.println(courses);
    }

    @Override
    public void close() throws IOException {
        try {
            SqlUtils.executeSqlScriptFile(datasource, "sql/drop_schema.sql");
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        String dbPropertiesFileName = args.length == 0 ? "db.properties" : args[0];
        Properties databaseProperties = loadPropertiesFromResources(dbPropertiesFileName);
        try (Datasource datasource = new SimpleDatasorce(databaseProperties);
             SchoolApp schoolApp = new SchoolApp(datasource);) {
            schoolApp.run();
        }
    }

}
