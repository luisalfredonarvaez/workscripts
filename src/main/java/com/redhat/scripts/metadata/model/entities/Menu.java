package com.redhat.scripts.metadata.model.entities;

import com.redhat.scripts.metadata.app.actions.InfoAction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class Menu
{
    @Setter(AccessLevel.NONE)
    private final UUID id;
    private final List<MenuOption> menuOptionList;
    private final Set<InfoAction> infoActions;

    public Menu(@NonNull List<Directory> directoryList, @NonNull Set<InfoAction> infoActions)
    {
        Objects.requireNonNull(directoryList);
        if (directoryList.isEmpty())
            throw new RuntimeException("Directory list cannot be empty. Programming error!");

        Objects.requireNonNull(infoActions);
        if (infoActions.isEmpty())
            throw new RuntimeException("InfoActions set cannot be empty. Programming error!");

        this.id = UUID.randomUUID();
        this.menuOptionList = directoryList.stream()
                .map(directory -> new MenuOption(directory))
                .toList();
        this.infoActions = infoActions;
    }
}
