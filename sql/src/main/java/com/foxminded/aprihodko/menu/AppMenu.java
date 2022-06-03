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
import com.foxminded.aprihodko.menu.screens.impl.custom.EditGroupScreen;
import com.foxminded.aprihodko.model.Group;
import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;

public class AppMenu {
    private final Console console;
    private final Datasource datasource;
    private final GroupDao groupDao;
    private final StudentDao studentsDaoImpl;
    private final CourseDao courseDaoImpl;

    public AppMenu(Console console,
                   Datasource datasource,
                   GroupDao groupDao,
                   StudentDao studentsDaoImpl,
                   CourseDao courseDaoImpl) {

        this.console = console;
        this.datasource = datasource;
        this.groupDao = groupDao;
        this.studentsDaoImpl = studentsDaoImpl;
        this.courseDaoImpl = courseDaoImpl;
    }

    public void run() {
        MenuScreen course = new StaticMenuScreen("courses", "Courses", listMakesForCourses());
        MenuScreen student = new StaticMenuScreen("students", "Students", listMakesForStudents());
        EditGroupScreen editGroups = new EditGroupScreen(datasource, groupDao);
        MenuScreen group = new StaticMenuScreen("groups", "Groups", Arrays.asList(
                new NavigateAction(editGroups.getTitle(), editGroups.getName()),
                new AbstractAction("Find all Groups", (console) -> findAllGroups(console)),
                new AbstractAction("Find all groups with less or equals student count", (console) -> findAllGroupsWithLessStudents(console))
        ));
        StaticMenuScreen main = new StaticMenuScreen("main",
                "Main menu",
                Arrays.asList(new NavigateAction(course), new NavigateAction(student), new NavigateAction(group)));
        Menu menu = new Menu(console, Arrays.asList(main, course, student, group, editGroups), Arrays.asList(editGroups.getGroupEditScreenHandler()));
        menu.show("main");
    }

    private String findAllGroups(Console console) {
        try {
            List<Group> groups = fromTransaction(datasource, connection -> groupDao.findAll(connection));
            AtomicInteger count = new AtomicInteger();
            String result = groups.stream().map(group -> String.format("%-2d. %s", count.incrementAndGet(), group.getName()))
                    .collect(Collectors.joining("\n"));
            console.println(result);
            return "groups";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String findAllGroupsWithLessStudents(Console console) {
        return null;
    }


    private List<Action> listMakesForGroups() {
        List<String> makes = Arrays.asList("Find all Groups", "Find all groups with less or equals student count");
        List<Action> result = new ArrayList<>();
        for (int i = 0; i < makes.size(); i++) {
            String make = makes.get(i);
            result.add(new NavigateAction(make, "makes for Groups"));
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
