package com.foxminded.aprihodko.menu.screens.impl.custom;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.DynamicNavigationHandler;
import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.actions.impl.CourseEditAction;
import com.foxminded.aprihodko.menu.actions.impl.NavigateAction;
import com.foxminded.aprihodko.menu.screens.impl.DynamicMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.model.Course;
import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;

public class EditCourseScreen extends DynamicMenuScreen {

    private final Datasource datasource;
    private final CourseDao courseDao;

    private final DynamicNavigationHandler courseNavigationHandler;

    public EditCourseScreen(Datasource datasource, CourseDao courseDao) {
        super("editCourse", "Edit Courses");
        this.datasource = datasource;
        this.courseDao = courseDao;
        courseNavigationHandler = createDynamicHandler(datasource, courseDao);
    }

    private DynamicNavigationHandler createDynamicHandler(Datasource datasource, CourseDao courseDao) {
        return screenName -> {
            String[] parts = screenName.split("_");
            if (parts.length != 2 || !"editCourse".equals(parts[0])) {
                throw new IllegalAccessError("EditCourseScreen createDynamicHandler");
            }
            long id = Long.parseLong(parts[1]);
            return handleCourseById(screenName, id);
        };
    }

    private StaticMenuScreen handleCourseById(String screenName, long id) {
        try {
            Optional<Course> mayBeCourse = fromTransaction(datasource,
                    connection -> courseDao.findById(connection, id));
            if (mayBeCourse.isEmpty()) {
                return null;
            }
            return new StaticMenuScreen(screenName, mayBeCourse.get().getName(),
                    Arrays.asList(new CourseEditAction(datasource, courseDao, mayBeCourse.get())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Action> getActions() {
        try {
            List<Course> courses = fromTransaction(datasource, courseDao::findAll);
            return courses.stream().map(course -> new NavigateAction(course.getName(), "editCourse_" + course.getId()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public DynamicNavigationHandler getCourseEditScreeenHandler() {
        return courseNavigationHandler;
    }
}
