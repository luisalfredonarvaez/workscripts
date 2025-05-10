package com.redhat.scripts.metadata.model.repository;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@Log4j2
public abstract class AbstractRepository<E, ID> implements CrudRepository<E, ID>
{
    protected AbstractRepositoryManager repositoryManager;
    public AbstractRepository(@NonNull AbstractRepositoryManager repositoryManager, Class<E> entityClass)
    {
        Objects.requireNonNull(repositoryManager);
        this.repositoryManager = repositoryManager;

        repositoryManager.addRepository(this, entityClass);
        log.debug("AbstractRepository initialized");
    }
}
