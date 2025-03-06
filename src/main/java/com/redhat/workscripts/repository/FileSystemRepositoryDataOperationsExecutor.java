package com.redhat.workscripts.repository;

import lombok.NonNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

import static com.redhat.workscripts.repository.FileSystemRepositoryOperator.DBFILES_DIRECTORY_PREFIX;

public class FileSystemRepositoryDataOperationsExecutor extends RepositoryDataOperationsExecutor
{
    private FileSystemRepositoryOperator fsRepositoryOperator;
    public FileSystemRepositoryDataOperationsExecutor(@NonNull RepositoryOperator repositoryOperator)
    {
        super(repositoryOperator);
        if (!(repositoryOperator instanceof FileSystemRepositoryOperator))
            throw new RuntimeException("Programming error!");

        fsRepositoryOperator = (FileSystemRepositoryOperator)repositoryOperator;
    }

    @Override
    public boolean hasDirectoriesInfo()
    {
        if (!fsRepositoryOperator.exists())
            return false;

        FilenameFilter filter = (file, s) -> s.startsWith(DBFILES_DIRECTORY_PREFIX);

        File dbPathDirectory = fsRepositoryOperator.getDbPathDirectory();
        Objects.requireNonNull(dbPathDirectory);
        String[] listDirectories = dbPathDirectory.list(filter);

        return (null!=listDirectories && listDirectories.length > 0);
    }

}
