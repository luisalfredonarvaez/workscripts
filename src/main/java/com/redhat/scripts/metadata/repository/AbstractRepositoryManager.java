package com.redhat.scripts.metadata.repository;

import com.redhat.scripts.metadata.config.ConfigPropertiesHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Setter
@Getter
public abstract class AbstractRepositoryManager
{
    private AbstractRepositoryManager() {};

    protected Map<Class, AbstractRepository> repositories;

    protected ConfigPropertiesHandler configPropertiesHandler;
    public<E> AbstractRepositoryManager(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
        repositories = new HashMap<>();
    }

    public boolean doesRepositoryExists(Class entityClass)
    {
        Objects.requireNonNull(entityClass);
        Objects.requireNonNull(this.repositories);
        return this.repositories.containsKey(entityClass);
    }

    public Optional<AbstractRepository> getRepository(Class entityClass)
            throws NullRepositoryException
    {
        if (!doesRepositoryExists(entityClass))
        {
            log.warn("Returning null repository, as it does not exist");
            return Optional.empty();
        }

        AbstractRepository repository = repositories.get(entityClass);
        if (null ==  repository)
            throw new NullRepositoryException();

        return Optional.of(repository);
    }

    public void addRepository(AbstractRepository repoClass, Class entityClass)
    {
        Objects.requireNonNull(this.repositories);
        Objects.requireNonNull(repoClass);
        Objects.requireNonNull(entityClass);

        this.repositories.put(entityClass, repoClass);
        log.debug("Repository added: {} with class {}", repoClass, entityClass);
    }

    public void deleteRepository(Class entityClass)
    {
        Objects.requireNonNull(this.repositories);
        Objects.requireNonNull(entityClass);

        this.repositories.remove(entityClass);
    }


    abstract public void initRepositoryManager();
    abstract public boolean repositoryManagerInitialized();
}
