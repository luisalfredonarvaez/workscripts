package com.redhat.scripts.metadata.services;

import com.redhat.scripts.metadata.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.entities.Directory;
import com.redhat.scripts.metadata.repository.NullRepositoryException;
import com.redhat.scripts.metadata.repository.AbstractRepository;
import com.redhat.scripts.metadata.repository.RepositoryFactory;
import com.redhat.scripts.metadata.repository.AbstractRepositoryManager;
import com.redhat.scripts.metadata.fetchers.DirectoriesFetcher;
import com.redhat.scripts.metadata.fetchers.FetcherFactory;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
public class ApplicationProcessesService
{
    private AbstractRepository<Directory, Long> directoriesRepository;
    private AbstractRepositoryManager repositoryManager;

    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    public ApplicationProcessesService(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
    }

    public void init()
            throws URISyntaxException
    {
        this.configPropertiesHandler.setupProperties();
        this.configPropertiesHandler.validateProperties();
        log.info("Config loaded");

        boolean repositoryManagerInitialized = checkIfRepositoryManagerInitialized();
        if (!repositoryManagerInitialized)
        {
            log.debug("Repository manager was not initialized previously!");
            repositoryManager.initRepositoryManager();
        }
        else
            log.debug("Repository manager was initialized previously!");

        boolean repositoryHasDirectoriesInfo = checkIfDirectoriesInfoExists();
        log.info("Repository exists: {}, repository has directories info: {}",
                repositoryManagerInitialized, repositoryHasDirectoriesInfo);

        FetcherFactory factory = new FetcherFactory(configPropertiesHandler);
        List<DirectoriesFetcher> directoriesFetcherList = factory.getFetchers();
        for (DirectoriesFetcher directoryFetcher: directoriesFetcherList)
        {
            directoryFetcher.fetchAllFromURI();
        }
        log.info("URIs fetched");

    }

    private boolean checkIfDirectoriesInfoExists()
    {
        this.repositoryManager = getRepositoryManager();
        this.directoriesRepository = getDirectoriesRepository();
        return (this.directoriesRepository.count() > 0);
    }

    private boolean checkIfRepositoryManagerInitialized()
    {
        this.repositoryManager = getRepositoryManager();
        return this.repositoryManager.repositoryManagerInitialized();
    }

    private AbstractRepositoryManager getRepositoryManager()
    {
        if (null == repositoryManager)
        {
            RepositoryFactory repositoryFactory = new RepositoryFactory(this.configPropertiesHandler);
            Objects.requireNonNull(repositoryFactory);
            this.repositoryManager = repositoryFactory.getRepositoryChecker(
                    configPropertiesHandler.getSupportedRepositoryType());

            Objects.requireNonNull(this.repositoryManager);
        }

        return this.repositoryManager;
    }

    private AbstractRepository getDirectoriesRepository()
    {
        if (null == this.directoriesRepository)
        {
            Objects.requireNonNull(this.repositoryManager);

            //not allowed null here!
            try
            {
                Optional<AbstractRepository>  directoriesRepository = this.repositoryManager.getRepository(Directory.class);

                if ( directoriesRepository.isEmpty())
                    throw new NullRepositoryException();

                this.directoriesRepository =  directoriesRepository.get();
            }
            catch (NullRepositoryException nree)
            {
                throw new RuntimeException(nree);
            }
        }

        return this.directoriesRepository;
    }

}
