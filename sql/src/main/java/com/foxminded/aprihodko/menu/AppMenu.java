package com.foxminded.aprihodko.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.actions.impl.AbstractAction;
import com.foxminded.aprihodko.menu.actions.impl.NavigateAction;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.menu.screens.MenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.EditCourseScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.EditGroupScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.EditStudentScreen;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.model.Group;
import com.foxminded.aprihodko.model.Students;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;

public class AppMenu {
    private final Console console;
    private final Datasource datasource;
    private final GroupDao groupDao;
    private final StudentDao studentsDao;
    private final CourseDao courseDao;

    public AppMenu(Console console, Datasource datasource, GroupDao groupDao, StudentDao studentsDao,
            CourseDao courseDao) {

        this.console = console;
        this.datasource = datasource;
        this.groupDao = groupDao;
        this.studentsDao = studentsDao;
        this.courseDao = courseDao;
    }

    public void run() {
        EditCourseScreen editCourse = new EditCourseScreen(datasource, courseDao);
        EditGroupScreen editGroups = new EditGroupScreen(datasource, groupDao);
        EditStudentScreen editStudent = new EditStudentScreen(datasource, studentsDao);
        MenuScreen students = menuScreenForStudents(editStudent);
        MenuScreen group = menuScreenForGroup(editGroups);
        MenuScreen course = menuScreenForCourse(editCourse);
        StaticMenuScreen main = new StaticMenuScreen("main", "Main menu",
                Arrays.asList(new NavigateAction(course), new NavigateAction(students), new NavigateAction(group)));
        Menu menu = new Menu(console, Arrays.asList(main, course, students, group, editGroups, editStudent, editCourse),
                Arrays.asList(editGroups.getGroupEditScreenHandler(), editStudent.getStudentEditScreenHandler(), editCourse.getCourseEditScreeenHandler()));
        menu.show("main");
    }
    
    
    private MenuScreen menuScreenForCourse(EditCourseScreen editCourse) {
        return new StaticMenuScreen("courses", "Courses", 
                                    Arrays.asList(new NavigateAction(editCourse.getTitle(), editCourse.getName()),
                                            new AbstractAction("Find all Courses", console -> findAllCourses(console))));
    }
    
    private MenuScreen menuScreenForGroup(EditGroupScreen editGroups) {
        return new StaticMenuScreen("groups", "Groups",
                Arrays.asList(new NavigateAction(editGroups.getTitle(), editGroups.getName()),
                        new AbstractAction("Find all Groups", (console) -> findAllGroups(console)),
                        new AbstractAction("Find all groups with less or equals student count",
                                (console) -> findAllGroupsWithLessStudents(console))));
    }
    
    private MenuScreen menuScreenForStudents(EditStudentScreen editStudent) {
        return new StaticMenuScreen("students", "Students", 
                Arrays.asList(new NavigateAction(editStudent.getTitle(), editStudent.getName()),
                        new AbstractAction("Find all Students", (console) -> findAllStudents(console))));
    }

    private String findAllStudents(Console console) {
        try {
            List<Students> students = fromTransaction(datasource, connection -> studentsDao.findAll(connection));
            AtomicInteger count = new AtomicInteger();
            String result = students.stream()
                    .map(student -> String.format("%-2d. %s", count.incrementAndGet(), student.getFirstName(), student.getLastName()))
                    .collect(Collectors.joining("\n"));
            console.println(result);
            return "students";
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    private String findAllGroups(Console console) {
        try {
            List<Group> groups = fromTransaction(datasource, connection -> groupDao.findAll(connection));
            AtomicInteger count = new AtomicInteger();
            String result = groups.stream()
                    .map(group -> String.format("%-2d. %s", count.incrementAndGet(), group.getName()))
                    .collect(Collectors.joining("\n"));
            console.println(result);
            return "groups";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String findAllCourses(Console console) {
        try {
            List<Course> courses = fromTransaction(datasource, connection -> courseDao.findAll(connection));
            AtomicInteger count = new AtomicInteger();
            String result = courses.stream()
                    .map(course -> String.format("%-2d. %s", count.incrementAndGet(), course.getName()))
                    .collect(Collectors.joining("\n"));
            console.println(result);
            return "courses";
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    private String findAllGroupsWithLessStudents(Console console) {
        return null;
    }
}
