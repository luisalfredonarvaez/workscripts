package com.redhat.scripts.metadata.model.entities;

import java.util.Objects;

public abstract class MenuOptionAction
{
    protected MenuOption menuOption;

    public MenuOptionAction(MenuOption menuOption)
    {
        Objects.requireNonNull(menuOption);
        this.menuOption = menuOption;
    }

    private String actionName;
    public abstract void execute();
}
