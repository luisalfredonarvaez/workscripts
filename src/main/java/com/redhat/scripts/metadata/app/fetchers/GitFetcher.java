package com.redhat.scripts.metadata.app.fetchers;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.model.entities.Directory;

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
    protected List<Directory> fetchAllFromURIImpl()
    {
        //TODO: Implement the logic to fetch directories from a Git repository
        return List.of();
    }
}
