package com.foxminded.aprihodko.menu;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.actions.ActionConstants;
import com.foxminded.aprihodko.menu.actions.impl.BackAction;
import com.foxminded.aprihodko.menu.actions.impl.ExitAction;
import com.foxminded.aprihodko.menu.console.Console;
import com.foxminded.aprihodko.menu.screens.MenuScreen;

public class Menu {

    private final Console console;
    private final Map<String, MenuScreen> screens;
    private final LinkedList<MenuScreen> breadCrumbs;

    public Menu(Console console) {
        this(console, new HashMap<>());
    }

    public Menu(Console console, Map<String, MenuScreen> screens) {
        this.console = console;
        this.screens = screens;
        breadCrumbs = new LinkedList<>();
    }

    public Menu(Console console, Collection<MenuScreen> screens) {
        this(console);
        screens.forEach(screen -> addScreen(screen));
    }

    public void addScreen(MenuScreen screen) {
        screens.put(screen.getName(), screen);
    }

    public void show(String screenName) {
        MenuScreen screen = screens.get(screenName);
        if (screen == null) {
            throw new IllegalStateException("MenuScreen '" + screenName + "' not defined");
        }
        play(screen);
    }

    private void play(MenuScreen screen) {
        Action exitAction = exitOrBack(breadCrumbs);
        console.print(screen.render(breadCrumbs, exitAction));
        int option = console.askForInteger("Select option", 0, screen.getActions().size());
        Action next = option == 0 ? exitAction : screen.getActions().get(option - 1);

        switch (next.apply(console)) {
        case ActionConstants.BACK:
            play(this.breadCrumbs.pop());
            break;
        case ActionConstants.EXIT:
            break;
        default:
            this.breadCrumbs.push(screen);
            show(next.apply(console));
        }
    }

    private Action exitOrBack(List<MenuScreen> breadCrumbs) {
        return breadCrumbs.isEmpty() ? new ExitAction() : new BackAction();
    }
}
