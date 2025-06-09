package com.redhat.scripts.metadata.tests.repositories.file;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.model.entities.Directory;
import com.redhat.scripts.metadata.model.repository.impl.FileSystemDirectoriesRepository;
import com.redhat.scripts.metadata.model.repository.impl.FileSystemRepositoryManager;
import com.redhat.scripts.metadata.tests.util.DefaultProperties;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TestFileSystemDirectoriesRepository
{
    @Test
    public void check1()
    {
        ConfigPropertiesHandler configPropertiesHandler = DefaultProperties.testDefaultConfigPropertiesHandler();
        configPropertiesHandler.setDefaultValues();
        FileSystemRepositoryManager fileSystemRepositoryManager = new FileSystemRepositoryManager(configPropertiesHandler);
        FileSystemDirectoriesRepository fileSystemDirectoriesRepository = new FileSystemDirectoriesRepository(fileSystemRepositoryManager);

        if (!fileSystemRepositoryManager.repositoryManagerInitialized())
            fileSystemRepositoryManager.initRepositoryManager();


        Directory directory = new Directory("/tmp");
        Directory savedDirectory = fileSystemDirectoriesRepository.save(directory);
        assert savedDirectory != null;
        assert savedDirectory.getId() != null;
        UUID uuid = savedDirectory.getId();
        Directory foundDirectory = fileSystemDirectoriesRepository.findById(uuid).orElse(null);
        assert foundDirectory != null;
        assert directory.getId().equals(foundDirectory.getId());
    }
}
