package com.redhat.scripts.metadata.model.entities;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class MenuOption
{
    final private UUID id;
    final private Directory directory;
    final private String description;
    final private List<MenuOptionAction> menuOptionActionList;

    public MenuOption(@NonNull Directory directory)
    {
        Objects.requireNonNull(directory);
        this.id = UUID.randomUUID();
        this.directory = directory;
        this.menuOptionActionList = new ArrayList<>();
        this.description = directory.getDiskLocation().getAbsolutePath();
        this.directory.setMenuOptionId(id);
    }

    public void addMenuOptionAction(MenuOptionAction menuOptionAction)
    {
        Objects.requireNonNull(menuOptionAction);
        this.menuOptionActionList.add(menuOptionAction);
    }
}
