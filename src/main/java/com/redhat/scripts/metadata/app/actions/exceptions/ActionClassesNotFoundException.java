package com.redhat.scripts.metadata.app.actions.exceptions;

import java.util.Collection;

public class ActionClassesNotFoundException extends RuntimeException
{
    private ActionClassesNotFoundException(String message)
    {
        super(message);
    }

    public ActionClassesNotFoundException(Collection<String> packageNames)
    {
        String fullMessage =
              String.format("No action classes found in the specified package(s): %s. Aborting application start.",
                      String.join(", ", packageNames));

        throw new ActionClassesNotFoundException(fullMessage);
    }
}
