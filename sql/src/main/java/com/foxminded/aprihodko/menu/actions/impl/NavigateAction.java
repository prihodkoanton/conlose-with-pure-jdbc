package com.foxminded.aprihodko.menu.actions.impl;

import com.foxminded.aprihodko.menu.screens.MenuScreen;

public class NavigateAction extends AbstractAction {

    public NavigateAction(String title, String targetScreenName) {
        super(title, console -> targetScreenName);
    }

    public NavigateAction(MenuScreen screen) {
        super(screen.getTitle(), console -> screen.getName());
    }
}
