package com.redhat.workscripts.repository;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import com.redhat.workscripts.config.SupportedRepositoryType;
import com.redhat.workscripts.config.WorkDirectory;
import lombok.NonNull;

import java.util.Objects;

public class RepositoryFactory
{
    private ConfigPropertiesHandler configPropertiesHandler;
    private WorkDirectory workDirectory;
    public RepositoryFactory(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
        this.workDirectory = configPropertiesHandler.getWorkDirectory();
        Objects.requireNonNull(this.workDirectory);

    }

    public RepositoryOperator getRepositoryChecker(SupportedRepositoryType supportedRepositoryType)
    {
        if (supportedRepositoryType == SupportedRepositoryType.FILE)
            return new FileSystemRepositoryOperator(this.configPropertiesHandler);

        throw new UnsupportedOperationException(
                String.format("Programming error: AbstractRepositoryChecker for '%s' not implented",
                        supportedRepositoryType.label));
    }
}
