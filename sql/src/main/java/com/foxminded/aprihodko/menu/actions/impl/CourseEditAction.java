package com.foxminded.aprihodko.menu.actions.impl;

import java.sql.SQLException;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.ActionConstants;
import com.foxminded.aprihodko.model.Course;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

public class CourseEditAction extends AbstractAction {
    private Datasource datasource;
    private CourseDao courseDao;

    public CourseEditAction(Datasource datasource, CourseDao courseDao, Course course) {
        super("Edit course '" + course.getName() + "' with dicreption: '" + course.getDescription() + "'", (console -> {
            String newName = console.askForString("Enter new course name");
            String newDiscription = console.askForString("Enter new course discription");
            if (!newName.isEmpty() && !newDiscription.isEmpty()) {
                course.setName(newName);
                course.setDescription(newDiscription);
                try {
                    inTransaction(datasource, connection -> courseDao.save(connection, course));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return ActionConstants.BACK;
            }
            return "";
        }));
        this.datasource = datasource;
        this.courseDao = courseDao;
    }
}
