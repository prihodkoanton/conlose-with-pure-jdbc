package com.foxminded.aprihodko.menu.screens;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.foxminded.aprihodko.menu.actions.Action;
import static  com.foxminded.aprihodko.menu.console.Console.EOL;

public interface MenuScreen {

    String getName();
    
    String getTitle();
    
    List<Action> getActions();
    
    default String render(List<MenuScreen> history, Action exitAction) {
        StringBuffer sb = new StringBuffer();
        String menuTitle = Stream.concat(history.stream(), Stream.of(this))
                .map(MenuScreen::getTitle)
                .collect(Collectors.joining(" > "));
        sb.append(String.format(" [ %s ]", menuTitle)).append(EOL);
        List<Action> actions = getActions();
        for(int i = 0; i < actions.size(); i ++) {
            sb.append(actions.get(i).render(i + 1)).append(EOL);
        }
        sb.append(exitAction.render(0)).append(EOL);
        return sb.toString();
    }
}
