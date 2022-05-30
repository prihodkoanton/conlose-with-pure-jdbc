package com.foxminded.aprihodko.menu.actions.impl;

import java.util.function.Function;

import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.console.Console;

public class AbstractAction implements Action{

    private final String title;
    private final Function<Console, String> action;
    
    public AbstractAction(String title, Function<Console, String> action) {
        this.title = title;
        this.action = action;
    }
    @Override
    public String apply(Console console) {
        return action.apply(console);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String render(int optionNum) {
        return String.format("%2d) %s", optionNum, getTitle());
    }
}
