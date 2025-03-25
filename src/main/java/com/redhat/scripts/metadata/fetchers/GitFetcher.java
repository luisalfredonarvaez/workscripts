package com.redhat.scripts.metadata.fetchers;

import com.redhat.scripts.metadata.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.entities.Directory;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class GitFetcher extends DirectoriesFetcher
{
    public GitFetcher(ConfigPropertiesHandler configPropertiesHandler, URI uri)
    {
        super(configPropertiesHandler);
        Objects.requireNonNull(uri);
        this.uri = uri;
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh()
    {

    }

    @Override
    public List<Directory> fetchAllFromURI()
    {
        return List.of();
    }
}
