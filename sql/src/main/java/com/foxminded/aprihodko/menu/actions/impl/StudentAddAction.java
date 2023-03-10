package com.foxminded.aprihodko.menu.actions.impl;

import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

import java.sql.SQLException;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.ActionConstants;
import com.foxminded.aprihodko.model.Students;

public class StudentAddAction extends AbstractAction {
    private static Datasource datasource;
    private static StudentDao studentDao;

    public StudentAddAction(Datasource datasource, StudentDao studentDao) {
        super("Add new student ", console -> {
            String newFirstName = console.askForString("Create first student name");
            String newLastName = console.askForString("Create last student name");
            String groupdId = console.askForString("Enter group_id for student");
            if (!newFirstName.isEmpty() && !newLastName.isEmpty() && !groupdId.isEmpty()) {
                Students student = new Students(Integer.valueOf(groupdId), newFirstName, newLastName);
                try {
                    inTransaction(datasource, connection -> studentDao.save(connection, student));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return ActionConstants.BACK;
            }
            return "";
        });
        this.datasource = datasource;
        this.studentDao = studentDao;
    }
}
