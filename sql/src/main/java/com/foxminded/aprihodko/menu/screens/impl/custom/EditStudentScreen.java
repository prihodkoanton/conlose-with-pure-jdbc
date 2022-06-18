package com.foxminded.aprihodko.menu.screens.impl.custom;

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
import com.foxminded.aprihodko.menu.actions.impl.StudentEditionAction;
import com.foxminded.aprihodko.menu.actions.impl.StudentRemoveAction;
import com.foxminded.aprihodko.menu.screens.MenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.DynamicMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.model.Students;
import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;

public class EditStudentScreen extends DynamicMenuScreen {
    private final Datasource datasource;
    private final StudentDao studentDao;

    private final DynamicNavigationHandler studentEditScreenHandler;

    public EditStudentScreen(Datasource datasource, StudentDao studentDao) {
        super("editStudents", "Edit Student");
        this.datasource = datasource;
        this.studentDao = studentDao;
        studentEditScreenHandler = createDynamicHandler(datasource, studentDao);
    }

    private DynamicNavigationHandler createDynamicHandler(Datasource datasource, StudentDao studentDao) {
        return screenName -> {
            String[] parts = screenName.split("_");
            if (parts.length != 2 || !"editStudent".equals(parts[0])) {
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
                throw new IllegalAccessError("handleStudentById");
            }
            return new StaticMenuScreen(screenName, mayBeStudent.get().getFirstName(),
                    Arrays.asList(new StudentEditionAction(datasource, studentDao, mayBeStudent.get())));
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
                            "editStudent_" + student.getId()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public DynamicNavigationHandler getStudentEditScreenHandler() {
        return studentEditScreenHandler;
    }

}
