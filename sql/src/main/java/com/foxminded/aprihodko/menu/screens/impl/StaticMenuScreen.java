package com.foxminded.aprihodko.menu.screens.impl;

import java.util.List;

import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.screens.MenuScreen;

public class StaticMenuScreen implements MenuScreen{

    private final String name;
    private final String title;
    private final List<Action> actions;
    
    public StaticMenuScreen(String name, String title, List<Action> actions) {
        this.name = name;
        this.title = title;
        this.actions = actions;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<Action> getActions() {
        return actions;
    }

}
