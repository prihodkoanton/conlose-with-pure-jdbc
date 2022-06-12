package com.foxminded.aprihodko.menu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.impl.AbstractAction;
import com.foxminded.aprihodko.menu.actions.impl.NavigateAction;
import com.foxminded.aprihodko.menu.actions.impl.StudentAddAction;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.menu.screens.MenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.DeleteStudentScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.EditCourseScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.EditGroupScreen;
import com.foxminded.aprihodko.menu.screens.impl.custom.EditStudentScreen;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.model.Group;
import com.foxminded.aprihodko.model.Students;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;;

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
        DeleteStudentScreen deleteStudent = new DeleteStudentScreen(datasource, studentsDao);
        MenuScreen students = menuScreenForStudents(editStudent, deleteStudent);
        MenuScreen group = menuScreenForGroup(editGroups);
        MenuScreen course = menuScreenForCourse(editCourse);
        StaticMenuScreen main = new StaticMenuScreen("main", "Main menu",
                Arrays.asList(new NavigateAction(course), new NavigateAction(students), new NavigateAction(group)));
        Menu menu = new Menu(console,
                Arrays.asList(main, course, students, group, editGroups, editStudent, editCourse, deleteStudent),
                Arrays.asList(editGroups.getGroupEditScreenHandler(), editStudent.getStudentEditScreenHandler(),
                        deleteStudent.getStudentDeleteScreenHandler(), editCourse.getCourseEditScreeenHandler()));
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

    private MenuScreen menuScreenForStudents(EditStudentScreen editStudent, DeleteStudentScreen deleteStuden) {
        return new StaticMenuScreen("students", "Students",
                Arrays.asList(new NavigateAction(editStudent.getTitle(), editStudent.getName()),
                        new NavigateAction(deleteStuden.getTitle(), deleteStuden.getName()),
                        new StudentAddAction(datasource, studentsDao),
                        new AbstractAction("Find all students related to course with given name",
                                (console) -> findAllStudentsRelatedToCourseWithGivenName(console)),
                        new AbstractAction("Remove the student from one his course", (console) -> removeTheStudentFromOneHisCourse(console)),
                        new AbstractAction("Find all Students", (console) -> findAllStudents(console))));
    }

    private String findAllStudents(Console console) {
        try {
            List<Students> students = fromTransaction(datasource, connection -> studentsDao.findAll(connection));
            AtomicInteger count = new AtomicInteger();
            String result = students.stream().map(student -> String.format("%2d) %s %s", count.incrementAndGet(),
                    student.getFirstName(), student.getLastName())).collect(Collectors.joining("\n"));
            String counts = String.valueOf(students.size());
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
                    .map(group -> String.format("%2d) %s", count.incrementAndGet(), group.getName()))
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
                    .map(course -> String.format("%2d) %s", count.incrementAndGet(), course.getName()))
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

    private String findAllStudentsRelatedToCourseWithGivenName(Console console) {
        try {
            List<Course> courses = fromTransaction(datasource, connection -> courseDao.findAll(connection));
            String courseName = console.askForString("Enter course name");
            // Need TODO
//            if (!courses.contains(courseName)) {
//                throw new IllegalArgumentException("Course with '" + courseName + "' name does not exitst");
//            }

            List<Students> students = fromTransaction(datasource,
                    connection -> studentsDao.findAllStudentRelatedToCourseWithGivenName(connection, courseName));
            AtomicInteger count = new AtomicInteger();
            String result = students.stream().map(student -> String.format("%2d) %s %s", count.incrementAndGet(),
                    student.getFirstName(), student.getLastName())).collect(Collectors.joining("\n"));
            console.println(result);
            return "students";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private String removeTheStudentFromOneHisCourse(Console console) {
        try {
            String student_id = console.askForString("Enter student id");
            String course_id = console.askForString("Enter course id");
            inTransaction(datasource, connetction -> studentsDao.removeTheStudentFromOneHisCourse(connetction, Long.valueOf(student_id), Long.valueOf(course_id)));
            return "students";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
