package com.foxminded.aprihodko;

import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.dao.datasource.SimpleDatasorce;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.dao.impl.GroupDaoImpl;
import com.foxminded.aprihodko.dao.impl.StudentsDaoImpl;
import com.foxminded.aprihodko.menu.AppMenu;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.menu.console.DefaultConsole;
import com.foxminded.aprihodko.misc.GenerateCourses;
import com.foxminded.aprihodko.misc.GenerateStudents;
import com.foxminded.aprihodko.misc.GeneratorGroups;
import com.foxminded.aprihodko.misc.StudentCoursesMappingGenerator;
import com.foxminded.aprihodko.utils.SqlUtils;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static com.foxminded.aprihodko.utils.ResourceUtils.loadPropertiesFromResources;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

public class SchoolApp implements Closeable {

    private final Datasource datasource;
    private final CourseDaoImpl courseDaoImpl;
    private final StudentsDaoImpl studentsDaoImpl;
    private final GroupDaoImpl groupDaoImpl;
    private final Console console;

    public SchoolApp(Datasource datasource, Console console) throws SQLException {
        this.datasource = datasource;
        SqlUtils.executeSqlScriptFile(datasource, "sql/create_schema.sql");
        this.courseDaoImpl = new CourseDaoImpl();
        this.studentsDaoImpl = new StudentsDaoImpl();
        this.groupDaoImpl = new GroupDaoImpl();
        this.console = console;
    }

    private void run() throws SQLException {
        inTransaction(datasource, (connection -> new GenerateStudents(studentsDaoImpl).generateData(connection, 200)));
        inTransaction(datasource, (connection -> new GeneratorGroups(groupDaoImpl).generateDate(connection, 10)));
        inTransaction(datasource, (connection -> new GenerateCourses(courseDaoImpl).generateDate(connection, 10)));
        inTransaction(datasource, connection -> new StudentCoursesMappingGenerator(studentsDaoImpl, courseDaoImpl)
                .mapStudentsToCourses(connection, 3, 5));
        AppMenu menu = new AppMenu(console, datasource, groupDaoImpl, studentsDaoImpl, courseDaoImpl);
        menu.run();
    }

    @Override
    public void close() throws IOException {
        try {
            SqlUtils.executeSqlScriptFile(datasource, "sql/drop_schema.sql");
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
    public static void main(String[] args) throws IOException, SQLException {
        String dbPropertiesFileName = args.length == 0 ? "db-docker.properties" : args[0];
        Properties databaseProperties = loadPropertiesFromResources(dbPropertiesFileName);
        try (Datasource datasource = new SimpleDatasorce(databaseProperties);
                Console console = new DefaultConsole(System.in, System.out);
                SchoolApp schoolApp = new SchoolApp(datasource, console)) {
            schoolApp.run();
        }
    }
}