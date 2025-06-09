package com.redhat.scripts.metadata.model.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.redhat.scripts.metadata.model.entities.Directory;
import com.redhat.scripts.metadata.model.repository.AbstractRepository;
import com.redhat.scripts.metadata.model.repository.AbstractRepositoryManager;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.redhat.scripts.metadata.model.repository.impl.FileSystemRepositoryManager.DBFILES_DIRECTORY_PREFIX;


@NoRepositoryBean
@Log4j2
public class FileSystemDirectoriesRepository extends AbstractRepository<Directory, UUID>
{
    private static final int MAX_FILE_SIZE = 1 << 30; // 1GB

    private final FileSystemRepositoryManager fileSystemRepositoryManager;
    private final ObjectMapper objectMapper;

    public FileSystemDirectoriesRepository(@NonNull AbstractRepositoryManager repositoryManager)
    {
        super(repositoryManager, Directory.class);
        if (!(repositoryManager instanceof FileSystemRepositoryManager))
            throw new RuntimeException("Programming error!");

          // This cannot be checked here, because the repository manager is not initialized
          // until the FileSystemRepositoryManager is created.
//        if (!repositoryManager.repositoryManagerInitialized())
//            throw new RuntimeException("Programming error!");

        this.fileSystemRepositoryManager = (FileSystemRepositoryManager)repositoryManager;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        log.debug(this.getClass().getSimpleName() + " initialized!");
    }

    @Override
    public long count()
    {
        validateInitialization();
        List<String> listDirectories = getDirectoriesFilenamesStartingWith(DBFILES_DIRECTORY_PREFIX);

        if (null == listDirectories)
            return 0;

        return listDirectories.size();
    }


    @Override
    public <S extends Directory> S save(S entity)
    {
        Objects.requireNonNull(entity, "Entity cannot be null");
        validateInitialization();

        String filename = DBFILES_DIRECTORY_PREFIX + entity.getId() + ".json";
        File file = new File(fileSystemRepositoryManager.getDbPathDirectory(), filename);

        if (file.exists())
        {
            log.warn("File already exists: " + file.getAbsolutePath() + ". Overwriting it.");
        }

        try
        {
            Files.writeString(file.toPath(), objectMapper.writeValueAsString(entity));
            log.debug("Directory saved to file: " + file.getAbsolutePath());
            return entity;
        }
        catch (IOException e)
        {
            log.error("Error saving directory to file: " + file.getAbsolutePath(), e);
            throw new RuntimeException("Error saving directory to file", e);
        }
    }


    @Override
    public <S extends Directory> Iterable<S> saveAll(Iterable<S> entities)
    {
        Objects.requireNonNull(entities, "Entities cannot be null");
        validateInitialization();

        List<Directory> directories = new ArrayList<>();
        for (S entity : entities)
        {
            if (null == entity)
            {
                log.warn("Skipping null entity in saveAll");
                continue;
            }
            directories.add(save(entity));
        }
        return (Iterable<S>) StreamSupport.stream(directories.spliterator(), false)
                .collect(Collectors.toList());

    }



    @Override
    public Optional<Directory> findById(UUID uuid)
    {
        //TODO
        Objects.requireNonNull(uuid, "ID cannot be null");
        validateInitialization();

        List<String> filenames = getDirectoriesFilenamesStartingWith(DBFILES_DIRECTORY_PREFIX + uuid);
        if (filenames.isEmpty())
        {
            log.debug("No directory found with ID: {}", uuid);
            return Optional.empty();
        }

        if (filenames.size() > 1)
        {
            String message = String.format("Multiple directories found with ID: %s. Programming error!", uuid);
            log.warn(message);
            throw new RuntimeException(message);
        }

        String filename = filenames.get(0);
        File file = new File(fileSystemRepositoryManager.getDbPathDirectory(), filename);

        if (!file.exists())
        {
            log.debug("Directory file does not exist: " + file.getAbsolutePath());
            return Optional.empty();
        }

        try
        {
            Directory directory = fetchDirectoryFromFile(file);
            return Optional.of(directory);
        }
        catch (IOException e)
        {
            log.error("Error fetching directory from file: " + file.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(UUID uuid)
    {
        //TODO
        return false;
    }

    @Override
    public Iterable<Directory> findAll()
    {
        //TODO
        List<String> filenames = getDirectoriesFilenamesStartingWith(DBFILES_DIRECTORY_PREFIX);
        if (null == filenames || filenames.size() == 0)
        {
            log.debug("No filenames for directories found starting with prefix: " + DBFILES_DIRECTORY_PREFIX);
            return List.of();
        }

        List<Directory> ret = filenames.stream()
                .map(filename -> {
                    try
                    {
                        Directory directory = fetchDirectoryFromPath(fileSystemRepositoryManager.getDbPathDirectory(), filename);
                        if (null == directory)
                        {
                            log.warn("Directory is null for filename: " + filename);
                            return null;
                        }
                        log.debug("Fetched directory from location: {}", directory.getDiskLocationPath());
                        return directory;
                    }
                    catch (IOException e)
                    {
                        log.warn("Error fetching directory from path: " + filename, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ret;
    }

    private void validateInitialization()
    {
        if (!fileSystemRepositoryManager.repositoryManagerInitialized())
        {
            throw new RuntimeException("Repository manager has not been initialized! Programming error!");
        }
    }

    private Directory fetchDirectoryFromPath(File parentPath, String filename)
            throws IOException
    {
        Objects.requireNonNull(parentPath);
        Objects.requireNonNull(filename);
        File directoryFile = new File(parentPath, filename);
        return fetchDirectoryFromFile(directoryFile);
    }

    private Directory fetchDirectoryFromFile(File directoryFile)
            throws IOException
    {
        Objects.requireNonNull(directoryFile);
        if (!directoryFile.exists() || directoryFile.isDirectory())
            throw new FileNotFoundException
                    ("Filename for directory does not exist or is not a file: " + directoryFile.getAbsolutePath());

        if (directoryFile.length() > MAX_FILE_SIZE)
            throw new IOException("File is too large to be read: " + directoryFile.getAbsolutePath());

        String fileContents = Files.readString(directoryFile.toPath());
        if (fileContents.isBlank())
            throw new IOException("File is empty: " + directoryFile.getAbsolutePath());

        Directory directory = objectMapper.readValue(fileContents, Directory.class);
        return directory;
    }

    @Override
    public Iterable<Directory> findAllById(Iterable<UUID> uuids)
    {
        //TODO
        return null;
    }

    private List<String> getDirectoriesFilenamesStartingWith(String strFilter)
    {
        Objects.requireNonNull(strFilter);
        FilenameFilter filter = (file, s) -> s.startsWith(strFilter);

        File dbPathDirectory = fileSystemRepositoryManager.getDbPathDirectory();
        Objects.requireNonNull(dbPathDirectory);
        String[] listDirectories = dbPathDirectory.list(filter);
        return Collections.unmodifiableList(
                null == listDirectories ? List.of() : List.of(listDirectories)
        );
    }

    @Override
    public void deleteById(UUID uuid)
    {
        //TODO
    }

    @Override
    public void delete(Directory entity)
    {
        //TODO
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids)
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
