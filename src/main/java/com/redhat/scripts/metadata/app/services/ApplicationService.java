package com.redhat.scripts.metadata.app.services;

import com.redhat.scripts.metadata.app.actions.InfoAction;
import com.redhat.scripts.metadata.app.actions.exceptions.ActionClassesNotFoundException;
import com.redhat.scripts.metadata.app.config.ActionsConfigPropertiesHandler;
import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.model.entities.Directory;
import com.redhat.scripts.metadata.model.entities.Menu;
import com.redhat.scripts.metadata.model.entities.MenuOptionAction;
import com.redhat.scripts.metadata.model.repository.NullRepositoryException;
import com.redhat.scripts.metadata.model.repository.AbstractRepository;
import com.redhat.scripts.metadata.model.repository.RepositoryFactory;
import com.redhat.scripts.metadata.model.repository.AbstractRepositoryManager;
import com.redhat.scripts.metadata.app.fetchers.DirectoriesFetcher;
import com.redhat.scripts.metadata.app.fetchers.FetcherFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.*;

@Log4j2
@Getter
@Service
public class ApplicationService
{
    private AbstractRepository<Directory, Long> directoriesRepository;
    private AbstractRepositoryManager repositoryManager;

    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    @Autowired
    private ActionsConfigPropertiesHandler actionsConfigPropertiesHandler;

    private Set<InfoAction> infoActions;
    private List<Directory> directoryList;

    private boolean repositoryHasDirectoriesInfo;

    public ApplicationService(@NonNull ConfigPropertiesHandler configPropertiesHandler,
                              @NonNull ActionsConfigPropertiesHandler actionsConfigPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        Objects.requireNonNull(actionsConfigPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
        this.actionsConfigPropertiesHandler = actionsConfigPropertiesHandler;
    }

    public Menu start()
            throws URISyntaxException, ActionClassesNotFoundException
    {
        infoActions = this.loadConfig();

        directoryList = this.buildDirectoryListFromDirectoryFetchers(this.loadDirectoriesWhenStarting());

        if (directoryList.isEmpty())
        {
            log.error("No directories found! Exiting application.");
            throw new RuntimeException("No directories found! Exiting application.");
        }

        Menu menu = new Menu(directoryList, infoActions);
        return menu;
    }

    private Set<InfoAction> loadConfig()
            throws ActionClassesNotFoundException
    {
        log.info("Config loaded: start");
        infoActions = actionsConfigPropertiesHandler.loadActionsProperties();
        this.configPropertiesHandler.setupProperties();
        this.configPropertiesHandler.validateProperties();
        log.info("Config loaded: end");
        return infoActions;
    }

    private List<Directory> buildDirectoryListFromDirectoryFetchers
            (List<DirectoriesFetcher> directoryFetchersList)
    {
        List<Directory> directoryList = new ArrayList<>();

        for (DirectoriesFetcher directoryFetcher : directoryFetchersList)
        {
            List<Directory> directories = directoryFetcher.getLastFetchedDirectories();
            if (directories != null)
            {
                directoryList.addAll(directories);
            }
        }

        List<Directory> uniqueDirectories = directoryList.stream()
                .sorted()
                .distinct()
                .toList();

        return uniqueDirectories;
    }


    private List<DirectoriesFetcher> loadDirectoriesWhenStarting()
            throws URISyntaxException
    {
        boolean repositoryManagerInitialized = checkIfRepositoryManagerInitialized();
        if (!repositoryManagerInitialized)
        {
            log.debug("Repository manager was not initialized previously!");
            repositoryManager.initRepositoryManager();
        }
        else
            log.debug("Repository manager was initialized previously!");

        repositoryHasDirectoriesInfo = checkIfDirectoriesInfoExists();
        log.info("Repository exists: {}, repository has directories info: {}",
                repositoryManagerInitialized, repositoryHasDirectoriesInfo);

        if (!repositoryHasDirectoriesInfo)
        {
            log.info("Fetching the repositories from the URIs, as the repository is empty");
            FetcherFactory factory = new FetcherFactory(configPropertiesHandler);
            List<DirectoriesFetcher> directoriesFetcherList = factory.getFetchers();
            for (DirectoriesFetcher directoryFetcher: directoriesFetcherList)
            {
                directoryFetcher.fetchAllFromURI();
            }
            log.info("URIs fetched");

            return directoriesFetcherList;
        }
        else
        {
            //TODO: Implement the logic to load directories from the repository
            return List.of();
        }

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
