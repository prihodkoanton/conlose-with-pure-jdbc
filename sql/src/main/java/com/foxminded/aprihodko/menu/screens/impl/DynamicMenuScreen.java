package com.foxminded.aprihodko.menu.screens.impl;

import java.util.List;

import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.screens.MenuScreen;

public class DynamicMenuScreen implements MenuScreen{

    private final String name;
    private final String title;
    private final List<Action> actions;
    
//    private final String name2;
//    private final Object title2 ;
//    private final Object actions2;
    
    
    public DynamicMenuScreen(String name, String title, List<Action> actions) {
        this.name = name;
        this.title = title;
        this.actions = actions;
    }
    
//    public DynamicMenuScreen(String name2, Object title2, Object actions2) {
//        this.name2 = name2;
//        this.title2 = title2;
//        this.actions2 =actions2;
//    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return (String) title;
    }

    @Override
    public List<Action> getActions() {
        return (List<Action>) actions;
    }
}
