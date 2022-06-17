package com.foxminded.aprihodko.menu.screens.impl.custom;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.datasource.Datasource;
import com.foxminded.aprihodko.menu.DynamicNavigationHandler;
import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.actions.impl.GroupEditAction;
import com.foxminded.aprihodko.menu.actions.impl.NavigateAction;
import com.foxminded.aprihodko.menu.screens.impl.DynamicMenuScreen;
import com.foxminded.aprihodko.menu.screens.impl.StaticMenuScreen;
import com.foxminded.aprihodko.model.Group;

import static com.foxminded.aprihodko.utils.TransactionUtils.fromTransaction;

public class EditGroupScreen extends DynamicMenuScreen {

    private final Datasource datasource;
    private final GroupDao groupDao;

    private final DynamicNavigationHandler groupEditScreenHandler;

    public EditGroupScreen(Datasource datasource, GroupDao groupDao) {
        super("editGroups", "Edit Group");
        this.datasource = datasource;
        this.groupDao = groupDao;
        groupEditScreenHandler = createDynamicHandler(datasource, groupDao);
    }

    private DynamicNavigationHandler createDynamicHandler(Datasource datasource, GroupDao groupDao) {
        return screenName -> {
            String[] parts = screenName.split("_");
            if (parts.length != 2 || !"editGroup".equals(parts[0])) {
                return null;
            }
            long id = Long.parseLong(parts[1]);
            return handleGroupById(screenName, id);
        };
    }

    private StaticMenuScreen handleGroupById(String screenName, long id) {
        try {
            Optional<Group> maybeGroup = fromTransaction(datasource, connection -> groupDao.findById(connection, id));
            if (maybeGroup.isEmpty()) {
                return null;
            }

            return new StaticMenuScreen(screenName, maybeGroup.get().getName(),
                    Arrays.asList(new GroupEditAction(datasource, groupDao, maybeGroup.get())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Action> getActions() {
        try {
            List<Group> groups = fromTransaction(datasource, groupDao::findAll);
            return groups.stream().map(group -> new NavigateAction(group.getName(), "editGroup_" + group.getId()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public DynamicNavigationHandler getGroupEditScreenHandler() {
        return groupEditScreenHandler;
    }

}
