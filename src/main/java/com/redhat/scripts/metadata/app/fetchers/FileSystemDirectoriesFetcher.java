package com.redhat.scripts.metadata.app.fetchers;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.model.entities.Directory;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
        List<File> filteredFiles;
        try
        {
            filteredFiles = filterFilesAccordingToScriptWildcard(new File(uri));
        }
        catch (FileFilterException e)
        {
            //TODO: Handle exception properly
            throw new RuntimeException("There were issues fetching the directories, cannot continue", e);
        }

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
    private List<File> filterFilesAccordingToScriptWildcard(File dir)
            throws FileFilterException
    {
        List<File> ret = new ArrayList<>();
        List<String> appScriptFilters = configPropertiesHandler.getAppScriptWildcards();
        log.debug("property appScriptFilters -> {}", appScriptFilters);

        try
        {
            for (String wildcard: appScriptFilters)
                listMatchingFiles(dir.getAbsolutePath(), wildcard, ret);
        }
        catch (IOException e)
        {
            log.error("Error listing files in directory: {}", dir.getAbsolutePath(), e);
            throw new FileFilterException(dir.getAbsolutePath(), e);
        }

        return ret;
    }

    public static void listMatchingFiles(String directoryPath, String wildcard, List<File> result) throws IOException
    {
        Pattern pattern = Pattern.compile(wildcardToRegex(wildcard));

        try (Stream<Path> files = Files.walk(Paths.get(directoryPath)))
        {
            files.filter(Files::isRegularFile)
                    .filter(path -> pattern.matcher(path.getFileName().toString()).matches())
                    .forEach(path -> {
                        File file = path.toFile();
                        log.debug("ACCEPTED File: {}", file.getAbsolutePath());
                        result.add(file);
                    });
        }
    }

    private static String wildcardToRegex(String wildcard)
    {
        return wildcard.replace(".", "\\.")
                        .replace("*", ".*")
                        .replace("?", ".")
                ;
    }

}
