package com.redhat.scripts.metadata.model.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Menu
{
    @Setter(AccessLevel.NONE)
    private UUID id;
    private List<MenuOption> menuOptionList;
    private boolean hasPendingChanges;

    public Menu(@NonNull List<Directory> directoryList)
    {
        this(directoryList, false);
    }

    public Menu(@NonNull List<Directory> directoryList, boolean hasPendingChanges)
    {
        Objects.requireNonNull(directoryList);
        this.id = UUID.randomUUID();
        this.menuOptionList = directoryList.stream()
                .map(directory -> new MenuOption(directory))
                .toList();
        this.hasPendingChanges = hasPendingChanges;
    }
}
