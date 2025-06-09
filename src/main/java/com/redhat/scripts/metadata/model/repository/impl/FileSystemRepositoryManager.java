package com.redhat.scripts.metadata.model.repository.impl;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.app.config.WorkDirectory;
import com.redhat.scripts.metadata.model.repository.AbstractRepositoryManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Objects;

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
            String currentPath = parent.getAbsolutePath() + File.separator + DBFILES_SUBDIR;
            this.dbPathDirectory = new File(currentPath);
        }
        Objects.requireNonNull(this.dbPathDirectory);
    }

    @Override
    public void initRepositoryManager()
    {
        if (repositoryManagerInitialized())
        {
            log.debug("Repository manager already initialized");
            return;
        }

        if (!this.dbPathDirectory.exists())
            this.dbPathDirectory.mkdir();

        fileSystemDirectoriesRepository = new FileSystemDirectoriesRepository(this);

        log.debug("Repository manager initialized for the first time");
    }

    @Override
    public boolean repositoryManagerInitialized()
    {
        Objects.requireNonNull(this.dbPathDirectory);

        return this.dbPathDirectory.exists() && (null!=fileSystemDirectoriesRepository);
    }
}
