package com.redhat.scripts.metadata.repository.impl;

import com.redhat.scripts.metadata.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.config.WorkDirectory;
import com.redhat.scripts.metadata.entities.Directory;
import com.redhat.scripts.metadata.repository.AbstractRepository;
import com.redhat.scripts.metadata.repository.AbstractRepositoryManager;
import com.redhat.scripts.metadata.repository.NullRepositoryException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Log4j2
public class FileSystemRepositoryManager extends AbstractRepositoryManager
{
    private FileSystemDirectoriesRepository fileSystemDirectoriesRepository;
    private File dbPathDirectory;
    public static final String DBFILES_SUBDIR="dbfiles";
    public static final String DBFILES_DIRECTORY_PREFIX="directory";

    private WorkDirectory workDirectory;

    public FileSystemRepositoryManager(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        super(configPropertiesHandler);
        this.workDirectory = configPropertiesHandler.getWorkDirectory();
        setDbPathDirectory(this.workDirectory);
        this.initRepositoryManager();
        log.debug(this.getClass().getSimpleName() + " initialized!");
    }

    private void setDbPathDirectory(WorkDirectory workDirectory)
    {
        Objects.requireNonNull(workDirectory);
        if (null == this.dbPathDirectory)
        {
            File parent = workDirectory.getWorkdirAsFile();
            String currentPath = parent.getAbsolutePath() + File.pathSeparator + DBFILES_SUBDIR;
            this.dbPathDirectory = new File(currentPath);
        }
        Objects.requireNonNull(this.dbPathDirectory);
    }

    @Override
    public void initRepositoryManager()
    {
        if (repositoryManagerInitialized())
        {
            Objects.requireNonNull(fileSystemDirectoriesRepository);
            log.debug("Repository manager already initialized");
            return;
        }

        if (!this.dbPathDirectory.exists())
            this.dbPathDirectory.mkdir();

        log.debug("Repository manager initialized for the first time");

        fileSystemDirectoriesRepository = new FileSystemDirectoriesRepository(this);


    }

    @Override
    public boolean repositoryManagerInitialized()
    {
        Objects.requireNonNull(this.dbPathDirectory);

        return this.dbPathDirectory.exists() && (null!=fileSystemDirectoriesRepository);
    }
}
