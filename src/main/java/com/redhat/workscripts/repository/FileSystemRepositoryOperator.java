package com.redhat.workscripts.repository;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import com.redhat.workscripts.config.WorkDirectory;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
public class FileSystemRepositoryOperator extends RepositoryOperator
{
    private File dbPathDirectory;
    public static final String DBFILES_SUBDIR="dbfiles";
    public static final String DBFILES_DIRECTORY_PREFIX="directory";

    private WorkDirectory workDirectory;

    public FileSystemRepositoryOperator(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        super(configPropertiesHandler);
        this.workDirectory = configPropertiesHandler.getWorkDirectory();
        setDbPathDirectory(this.workDirectory);
    }

    @Override
    public boolean exists()
    {
        Objects.requireNonNull(this.workDirectory);
        Objects.requireNonNull(this.dbPathDirectory);
        return this.dbPathDirectory.exists() && this.dbPathDirectory.isDirectory();
    }

    //TODO
    @Override
    public RepositoryDataOperationsExecutor createRepository()
            throws NullRepositoryExecutorException
    {
        Objects.requireNonNull(this.dbPathDirectory);

        if (!this.dbPathDirectory.exists())
            this.dbPathDirectory.mkdir();

        Optional<RepositoryDataOperationsExecutor> opExec = getExecutor();

        if (opExec.isEmpty())
            throw new NullRepositoryExecutorException();

        return opExec.get();
    }

    @Override
    public void deleteRepository()
    {
        if (!this.dbPathDirectory.delete())
        {
            //TODO
        }
    }

    //TODO
    @Override
    protected RepositoryDataOperationsExecutor createExecutor()
    {
        return null;
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
}
