package com.foxminded.aprihodko.menu.actions.impl;

import com.foxminded.aprihodko.menu.actions.ActionConstants;

public class ExitAction extends AbstractAction {

    public ExitAction() {
        super("Exit", console -> ActionConstants.EXIT);
    }
}
