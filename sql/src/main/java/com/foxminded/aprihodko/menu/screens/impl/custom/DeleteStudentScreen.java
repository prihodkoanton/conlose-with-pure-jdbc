package com.foxminded.aprihodko.menu.screens.impl.custom;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.DynamicNavigationHandler;
import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.actions.impl.NavigateAction;
import com.foxminded.aprihodko.menu.actions.impl.StudentRemoveAction;
import com.foxminded.aprihodko.menu.screens.MenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.DynamicMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.model.Students;

public class DeleteStudentScreen extends DynamicMenuScreen {

    private final Datasource datasource;
    private final StudentDao studentDao;

    private final DynamicNavigationHandler studentDeleteScreenHandler;

    public DeleteStudentScreen(Datasource datasource, StudentDao studentDao) {
        super("deleteSsudent", "Delete student");
        this.datasource = datasource;
        this.studentDao = studentDao;
        studentDeleteScreenHandler = createDynamicHabdler(datasource, studentDao);
    }

    private DynamicNavigationHandler createDynamicHabdler(Datasource datasource, StudentDao studentDao) {
        return screenName -> {
            String[] parts = screenName.split("_");
            if (parts.length != 2 || !"deleteStudent".equals(parts[0])) {
                return null;
            }
            long id = Long.parseLong(parts[1]);
            return handleStudentById(screenName, id);
        };
    }

    private MenuScreen handleStudentById(String screenName, long id) {
        try {
            Optional<Students> mayBeStudent = fromTransaction(datasource,
                    connection -> studentDao.findById(connection, id));
            if (mayBeStudent.isEmpty()) {
                throw new IllegalArgumentException("The student with id: '" + id + "' does not exist");
            }
            return new StaticMenuScreen(screenName,
                    mayBeStudent.get().getFirstName() + " " + mayBeStudent.get().getLastName(),
                    Arrays.asList(new StudentRemoveAction(datasource, studentDao, mayBeStudent.get())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Action> getActions() {
        try {
            List<Students> students = fromTransaction(datasource, studentDao::findAll);
            return students.stream()
                    .map(student -> new NavigateAction(student.getFirstName() + " " + student.getLastName(),
                            "deleteStudent_" + student.getId()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public DynamicNavigationHandler getStudentDeleteScreenHandler() {
        return studentDeleteScreenHandler;
    }
}
