package com.redhat.scripts.metadata.app.fetchers;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.model.entities.Directory;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URI;
import java.util.*;

@Log4j2
public class FileSystemDirectoriesFetcher extends DirectoriesFetcher
{
    public FileSystemDirectoriesFetcher(ConfigPropertiesHandler configPropertiesHandler, URI uri)
    {
        super(configPropertiesHandler);
        Objects.requireNonNull(uri);
        this.uri = uri;
    }

    @Override
    public void refresh()
    {

    }

    @Override
    protected List<Directory> fetchAllFromURIImpl()
    {
        List<File> filteredFiles = filterFilesAccordingToScriptFilter(new File(uri));
        log.debug("Filtered list: {}", filteredFiles);

        List<Directory> directories = new ArrayList<>();
        for (File file : filteredFiles)
        {
            //Add parent folder
            Directory directory = new Directory(file.getParentFile());

            //Check if directory already exists
            if (!directories.contains(directory))
                directories.add(directory);
        }

        return directories;
    }


    //https://www.baeldung.com/java-list-files-recursively
    private List<File> filterFilesAccordingToScriptFilter(File dir)
    {
        List<File> ret = new ArrayList<>();
        List<String> scriptEnvFilters = configPropertiesHandler.getScriptEnvFilters();
        log.debug("property scriptEnvFilters -> ", scriptEnvFilters);

        Iterator<File> fileIterator = FileUtils.iterateFiles(dir, null, true);
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            String fileName = file.getName();
            log.debug("|{}|", fileName);
            if (!scriptEnvFilters.contains(fileName))
                log.debug("REJECTED File: {}", file.getAbsolutePath());
            else
            {
                log.debug("ACCEPTED File: {}", file.getAbsolutePath());
                ret.add(file);
            }
        }

        return ret;
    }
}
