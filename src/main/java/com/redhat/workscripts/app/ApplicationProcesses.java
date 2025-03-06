package com.redhat.workscripts.app;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import com.redhat.workscripts.repository.*;
import com.redhat.workscripts.fetchers.DirectoriesFetcher;
import com.redhat.workscripts.fetchers.FetcherFactory;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
public class ApplicationProcesses
{
    private RepositoryDataOperationsExecutor executor;
    private RepositoryOperator repositoryOperator;

    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    public ApplicationProcesses(@NonNull ConfigPropertiesHandler configPropertiesHandler)
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

        boolean repositoryExists = checkIfRepositoryExists();
        boolean repositoryHasDirectoriesInfo = checkIfRepositoryHasDirectoriesInfo();
        log.info("Repository exists: {}, repository has directories info: {}", repositoryExists, repositoryHasDirectoriesInfo);
        FetcherFactory factory = new FetcherFactory(configPropertiesHandler);
        List<DirectoriesFetcher> directoriesFetcherList = factory.getFetchers();
        for (DirectoriesFetcher directoryFetcher: directoriesFetcherList)
        {
            directoryFetcher.fetchAllFromURI();
        }
        log.info("URIs fetched");

    }

    private boolean checkIfRepositoryHasDirectoriesInfo()
    {
        this.repositoryOperator = getRepositoryOperator();
        this.executor = getRepositoryDataOperationsExecutor();
        return this.executor.hasDirectoriesInfo();
    }

    private boolean checkIfRepositoryExists()
    {
        this.repositoryOperator = getRepositoryOperator();
        return this.repositoryOperator.exists();
    }

    private RepositoryOperator getRepositoryOperator()
    {
        if (null == repositoryOperator)
        {
            RepositoryFactory repositoryFactory = new RepositoryFactory(this.configPropertiesHandler);
            Objects.requireNonNull(repositoryFactory);
            this.repositoryOperator = repositoryFactory.getRepositoryChecker(
                    configPropertiesHandler.getSupportedRepositoryType());

            Objects.requireNonNull(this.repositoryOperator);
        }

        return this.repositoryOperator;
    }

    private RepositoryDataOperationsExecutor getRepositoryDataOperationsExecutor()
    {
        if (null == this.executor)
        {
            Objects.requireNonNull(this.repositoryOperator);

            //not allowed null here!
            try
            {
                Optional<RepositoryDataOperationsExecutor> opExec = this.repositoryOperator.getExecutor();

                if (opExec.isEmpty())
                    throw new NullRepositoryExecutorException();

                this.executor = opExec.get();
            }
            catch (NullRepositoryExecutorException nree)
            {
                throw new RuntimeException(nree);
            }
        }

        return this.executor;
    }

}
