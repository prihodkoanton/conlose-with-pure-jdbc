package com.foxminded.aprihodko;

import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.dao.datasource.SimpleDatasorce;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.dao.impl.GroupDaoImpl;
import com.foxminded.aprihodko.dao.impl.StudentsDaoImpl;
import com.foxminded.aprihodko.menu.Menu;
import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.actions.impl.NavigateAction;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.menu.console.DefaultConsole;
import com.foxminded.aprihodko.menu.screens.MenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.DynamicMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.misc.GenerateCourses;
import com.foxminded.aprihodko.misc.GenerateStudents;
import com.foxminded.aprihodko.misc.GeneratorGroups;
import com.foxminded.aprihodko.misc.StudentCoursesMappingGenerator;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.model.Group;
import com.foxminded.aprihodko.model.Students;
import com.foxminded.aprihodko.utils.SqlUtils;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.foxminded.aprihodko.utils.ResourceUtils.loadPropertiesFromResources;
import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
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
//        List<Students> students = fromTransaction(datasource,
//                connection -> studentsDaoImpl.findByCourseId(connection, 4L));
//        List<Group> groups = fromTransaction(datasource, groupDaoImpl::findAll);
//        List<Course> courses = fromTransaction(datasource, courseDaoImpl::findAll);
//        List<Course> courses1 = fromTransaction(datasource,
//                connection -> courseDaoImpl.findByStudentId(connection, 4L));

        // for Menu
        MenuScreen course = new DynamicMenuScreen("courses", "Courses", listMakesForCourses());
        MenuScreen student = new DynamicMenuScreen("students", "Students", listMakesForStudents());
        MenuScreen group = new DynamicMenuScreen("groups", "Groups", listMakesForGroups());
        MenuScreen school = new StaticMenuScreen("School", "Welcome to School",
                Arrays.asList(new NavigateAction(group), new NavigateAction(student), new NavigateAction(course)));
        StaticMenuScreen main = new StaticMenuScreen("main", "Main menu", Arrays.asList(new NavigateAction(school)));
        Menu menu = new Menu(console, Arrays.asList(main, school, course, student, group));
        menu.show("main");
    }

    @Override
    public void close() throws IOException {
        try {
//            SqlUtils.executeSqlScriptFile(datasource, "sql/drop_schema.sql");
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

    private List<Action> listMakesForGroups() {
        List<String> makes = Arrays.asList("Find all Groups", "Find all groups with less or equals student count");
        List<Action> result = new ArrayList<>();
        for (int i = 0; i < makes.size(); i++) {
            String make = makes.get(i);
            result.add(new NavigateAction(make, "make for Groups"));
        }
        return result;
    }

    private List<Action> listMakesForStudents() {
        List<String> makes = Arrays.asList("Find all students related to course with given name", "Add new student",
                "Delete student by id", "Add a student to the course", "Remove the student from one or more courses");
        List<Action> result = new ArrayList<>();
        for (int i = 0; i < makes.size(); i++) {
            String make = makes.get(i);
            result.add(new NavigateAction(make, "makes for Students"));
        }
        return result;
    }

    private List<Action> listMakesForCourses() {
        List<String> makes = Arrays.asList("Need", "Need To");
        List<Action> result = new ArrayList<>();
        result.add(new NavigateAction("Add new make NAVIGATION", "add make form Navigation"));
        for (int i = 0; i < makes.size(); i++) {
            String make = makes.get(i);
            result.add(new NavigateAction(make, "make second Navigation"));
        }
        return result;
    }
}
