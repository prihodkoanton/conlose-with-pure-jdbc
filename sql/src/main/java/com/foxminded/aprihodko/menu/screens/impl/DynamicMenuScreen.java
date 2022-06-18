package com.foxminded.aprihodko.menu.screens.impl;

import java.util.List;

import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.screens.MenuScreen;

public abstract class DynamicMenuScreen implements MenuScreen {

    private final String name;
    private final String title;

    public DynamicMenuScreen(String name, String title) {
        this.name = name;
        this.title = title;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public abstract List<Action> getActions();
}
