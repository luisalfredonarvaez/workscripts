package com.redhat.scripts.metadata.app.actions;

import java.util.Objects;

public record InfoAction (Class aClass, String actionName)
{
    public InfoAction
    {
        Objects.requireNonNull(aClass);
        Objects.requireNonNull(actionName);
    }
}
