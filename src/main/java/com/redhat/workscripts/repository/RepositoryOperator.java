package com.redhat.workscripts.repository;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Setter
@Getter
public abstract class RepositoryOperator
{
    protected RepositoryDataOperationsExecutor executor;
    protected ConfigPropertiesHandler configPropertiesHandler;
    public RepositoryOperator(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
    }

    public Optional<RepositoryDataOperationsExecutor> getExecutor()
            throws NullRepositoryExecutorException
    {
        if (!exists())
        {
            log.debug("Returning null executor, as the repository does not exist");
            return Optional.empty();
        }

        if (null == executor)
            executor = createExecutor();

        if (null == executor)
            throw new NullRepositoryExecutorException();

        return Optional.of(executor);
    }

    abstract public boolean exists();
    abstract public RepositoryDataOperationsExecutor createRepository();
    abstract public void deleteRepository();
    abstract protected RepositoryDataOperationsExecutor createExecutor();
}
