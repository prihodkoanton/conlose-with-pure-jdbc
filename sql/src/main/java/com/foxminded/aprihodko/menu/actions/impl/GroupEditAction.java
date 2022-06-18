package com.foxminded.aprihodko.menu.actions.impl;

import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.actions.ActionConstants;
import com.foxminded.aprihodko.model.Group;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

import java.sql.SQLException;

public class GroupEditAction extends AbstractAction {
    private final Datasource datasource;
    private final GroupDao groupDaoImpl;

    public GroupEditAction(Datasource datasource, GroupDao groupDao, Group group) {
        super("Edit group " + group.getName(), (console -> {
            String newName = console.askForString("Enter new group name");
            if (!newName.isEmpty()) {
                group.setName(newName);
                try {
                    inTransaction(datasource, connection -> groupDao.save(connection, group));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return ActionConstants.BACK;
            }
            return "";
        }));
        this.datasource = datasource;
        this.groupDaoImpl = groupDao;
    }
}
