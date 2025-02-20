package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.entities.Directory;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Log4j2
public class FileSystemFetcher extends AbstractDirectoriesFetcher
{
    public FileSystemFetcher(URI uri)
    {
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
        listFilesCommonsIO(new File(uri));
        return List.of();
    }


    //https://www.baeldung.com/java-list-files-recursively
    private void listFilesCommonsIO(File dir) {
        Iterator<File> fileIterator = FileUtils.iterateFiles(dir, null, true);
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            log.info("File: " + file.getAbsolutePath());
        }
    }
}
