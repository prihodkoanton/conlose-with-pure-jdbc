package com.foxminded.aprihodko.menu.actions.impl;

import java.sql.SQLException;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.ActionConstants;
import com.foxminded.aprihodko.model.Students;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

public class StudentRemoveAction extends AbstractAction {

    private final Datasource datasource;
    private final StudentDao studentDao;

    public StudentRemoveAction(Datasource datasource, StudentDao studentDao, Students student) {
        super("Delete student " + student.getFirstName() + " " + student.getLastName() + " with id'" + student.getId() +"'", console -> {
                try {
                    inTransaction(datasource, connection -> studentDao.deleteById(connection, student.getId()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return ActionConstants.BACK;
        });
        this.datasource = datasource;
        this.studentDao = studentDao;
    }
}
