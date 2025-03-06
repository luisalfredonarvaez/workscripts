package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import com.redhat.workscripts.entities.Directory;
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
    public List<Directory> fetchAllFromURI()
    {
        List<File> filteredFiles = filterFilesAccordingToScriptFilter(new File(uri));
        log.debug("Filtered list: {}", filteredFiles);
        return List.of();
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
