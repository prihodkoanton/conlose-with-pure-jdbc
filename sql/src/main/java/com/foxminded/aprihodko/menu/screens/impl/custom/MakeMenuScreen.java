package com.foxminded.aprihodko.menu.screens.impl.custom;

import java.util.List;

import com.foxminded.aprihodko.menu.actions.Action;
import com.foxminded.aprihodko.menu.screens.impl.DynamicMenuScreen;

public class MakeMenuScreen extends DynamicMenuScreen{

public MakeMenuScreen(String name, String title, List<Action> actions) {
        super(name, title, actions);
        // TODO Auto-generated constructor stub
    }

//    public MakeMenuScreen() {
//        super("makes", () -> "Makes", () -> allMakeActions());
//    }
    
    private List<Action> allMakeActions(){
        return null;
    }
}
