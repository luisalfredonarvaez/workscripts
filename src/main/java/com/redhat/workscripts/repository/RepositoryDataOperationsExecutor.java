package com.redhat.workscripts.repository;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public abstract class RepositoryDataOperationsExecutor
{
    protected RepositoryOperator repositoryOperator;
    public  RepositoryDataOperationsExecutor(@NonNull RepositoryOperator repositoryOperator)
    {
        Objects.requireNonNull(repositoryOperator);
        this.repositoryOperator = repositoryOperator;
    }

    abstract public boolean hasDirectoriesInfo();
}
