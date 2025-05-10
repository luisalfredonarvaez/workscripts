package com.redhat.scripts.metadata.model.repository;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.app.config.SupportedRepositoryType;
import com.redhat.scripts.metadata.app.config.WorkDirectory;
import com.redhat.scripts.metadata.model.repository.impl.FileSystemRepositoryManager;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
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

    public AbstractRepositoryManager getRepositoryChecker(SupportedRepositoryType supportedRepositoryType)
    {
        if (supportedRepositoryType == SupportedRepositoryType.FILE)
        {
            log.debug("returning {} for SupportedRepositoryType {}", FileSystemRepositoryManager.class, supportedRepositoryType);
            return new FileSystemRepositoryManager(this.configPropertiesHandler);
        }

        throw new UnsupportedOperationException(
                String.format("Programming error: AbstractRepositoryChecker for '%s' not implented",
                        supportedRepositoryType.label));
    }
}
