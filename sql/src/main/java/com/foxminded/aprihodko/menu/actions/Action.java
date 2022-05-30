package com.foxminded.aprihodko.menu.actions;

import java.util.function.Function;

import com.foxminded.aprihodko.menu.console.Console;

public interface Action extends Function<Console, String>{
    
    String getTitle();
    String render(int optionNum);
}
