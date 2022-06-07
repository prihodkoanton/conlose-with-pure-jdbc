package com.foxminded.aprihodko.menu.actions.impl;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.ActionConstants;
import com.foxminded.aprihodko.model.Students;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

import java.sql.SQLException;

public class StudentEditionAction extends AbstractAction{
    private final Datasource datasource;
    private final StudentDao studentDao;
    
    public StudentEditionAction(Datasource datasource, StudentDao studentDao, Students students) {
        super("Edit student " + students.getFirstName() +" "+ students.getLastName(), console -> {
            String newFirstName = console.askForString("Enter new firt student name");
            String newLastName = console.askForString("Enter new last student name");
            if(!newFirstName.isEmpty()) {
                students.setFirstName(newFirstName);
                students.setLastName(newLastName);
                try {
                    inTransaction(datasource, connection -> studentDao.save(connection, students));
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
