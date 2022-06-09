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
        super("Delete student " + student.getId() + student.getFirstName() + " " + student.getLastName(), console -> {
            String idForDelete = console.askForString("Enter student id who you want delete");
            if (!idForDelete.isEmpty()) {
                long id = Long.parseLong(idForDelete);
                try {
                    inTransaction(datasource, connection -> studentDao.deleteById(connection, id));
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
