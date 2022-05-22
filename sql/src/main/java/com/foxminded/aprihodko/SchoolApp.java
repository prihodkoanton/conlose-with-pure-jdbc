package com.foxminded.aprihodko;

import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.dao.datasource.SimpleDatasorce;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.dao.impl.GroupDaoImpl;
import com.foxminded.aprihodko.dao.impl.StudentsDaoImpl;
import com.foxminded.aprihodko.misc.GenerateCourses;
import com.foxminded.aprihodko.misc.GenerateStudents;
import com.foxminded.aprihodko.misc.GeneratorGroups;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.model.Group;
import com.foxminded.aprihodko.model.Students;
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
    private final CourseDaoImpl courseDao;
    private final StudentsDaoImpl studentsDaoImpl;
    private final GroupDaoImpl groupDaoImpl;

    public SchoolApp(Datasource datasource) throws SQLException {
        this.datasource = datasource;
        SqlUtils.executeSqlScriptFile(datasource, "sql/create_schema.sql");
        this.courseDao = new CourseDaoImpl();
        this.studentsDaoImpl = new StudentsDaoImpl();
        this.groupDaoImpl = new GroupDaoImpl();
    }

    private void run() throws SQLException {
        inTransaction(datasource, (connection -> new GenerateCourses(courseDao).generateDate(connection, 10)));
        List<Course> courses = fromTransaction(datasource, courseDao::findAll);
        courses.forEach(System.out::println);
    }

    @Override
    public void close() throws IOException {
        try {
            SqlUtils.executeSqlScriptFile(datasource, "sql/drop_schema.sql");
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
//    throws IOException, SQLException
    public static void main(String[] args)  throws IOException, SQLException{
        String dbPropertiesFileName = args.length == 0 ? "db.properties" : args[0];
        Properties databaseProperties = loadPropertiesFromResources(dbPropertiesFileName);
        try (Datasource datasource = new SimpleDatasorce(databaseProperties);
                SchoolApp schoolApp = new SchoolApp(datasource)) {
            schoolApp.run();
        }
//        System.out.println("Hi all");
    }
}
