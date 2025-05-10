package com.redhat.scripts.metadata.model.repository.impl;

import com.redhat.scripts.metadata.model.entities.Directory;
import com.redhat.scripts.metadata.model.repository.AbstractRepository;
import com.redhat.scripts.metadata.model.repository.AbstractRepositoryManager;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;
import java.util.Optional;

import static com.redhat.scripts.metadata.model.repository.impl.FileSystemRepositoryManager.DBFILES_DIRECTORY_PREFIX;


@NoRepositoryBean
@Log4j2
public class FileSystemDirectoriesRepository extends AbstractRepository<Directory, Long>
{
    private FileSystemRepositoryManager fileSystemRepositoryManager;
    public FileSystemDirectoriesRepository(@NonNull AbstractRepositoryManager repositoryManager)
    {
        super(repositoryManager, Directory.class);
        if (!(repositoryManager instanceof FileSystemRepositoryManager))
            throw new RuntimeException("Programming error!");

        fileSystemRepositoryManager = (FileSystemRepositoryManager)repositoryManager;
        log.debug(this.getClass().getSimpleName() + " initialized!");
    }

    @Override
    public <S extends Directory> S save(S entity)
    {
        //TODO
        return null;
    }

    @Override
    public <S extends Directory> Iterable<S> saveAll(Iterable<S> entities)
    {
        //TODO
        return null;
    }

    @Override
    public Optional<Directory> findById(Long aLong)
    {
        //TODO
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong)
    {
        //TODO
        return false;
    }

    @Override
    public Iterable<Directory> findAll()
    {
        //TODO
        return null;
    }

    @Override
    public Iterable<Directory> findAllById(Iterable<Long> longs)
    {
        //TODO
        return null;
    }

    @Override
    public long count()
    {
        if (!fileSystemRepositoryManager.repositoryManagerInitialized())
        {
            log.debug("Returning 0 as repository manager has not been initialized");
            return 0;
        }

        FilenameFilter filter = (file, s) -> s.startsWith(DBFILES_DIRECTORY_PREFIX);

        File dbPathDirectory = fileSystemRepositoryManager.getDbPathDirectory();
        Objects.requireNonNull(dbPathDirectory);
        String[] listDirectories = dbPathDirectory.list(filter);

        if (null == listDirectories)
            return 0;

        return listDirectories.length;
    }

    @Override
    public void deleteById(Long aLong)
    {
        //TODO
    }

    @Override
    public void delete(Directory entity)
    {
        //TODO
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs)
    {
        //TODO
    }

    @Override
    public void deleteAll(Iterable<? extends Directory> entities)
    {
        //TODO
    }

    @Override
    public void deleteAll()
    {
        //TODO
    }
}
