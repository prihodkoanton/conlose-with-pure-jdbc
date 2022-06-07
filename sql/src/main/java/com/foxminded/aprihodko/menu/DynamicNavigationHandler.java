package com.foxminded.aprihodko.menu;

import java.util.function.Function;

import com.foxminded.aprihodko.menu.screens.MenuScreen;

public interface DynamicNavigationHandler extends Function<String, MenuScreen>{

}
