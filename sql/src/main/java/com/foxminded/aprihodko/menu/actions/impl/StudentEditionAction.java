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
    
    public StudentEditionAction(Datasource datasource, StudentDao studentDao, Students student) {
        super("Edit student " + student.getFirstName() +" "+ student.getLastName(), console -> {
            String newFirstName = console.askForString("Enter new first student name");
            String newLastName = console.askForString("Enter new last student name");
            if(!newFirstName.isEmpty() && !newLastName.isEmpty()) {
                student.setFirstName(newFirstName);
                student.setLastName(newLastName);
                try {
                    inTransaction(datasource, connection -> studentDao.save(connection, student));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return ActionConstants.BACK;
            }
            return "students";
        });
        this.datasource = datasource;
        this.studentDao = studentDao;
    }
}
