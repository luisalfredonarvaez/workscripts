package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.entities.Directory;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class GitFetcher extends AbstractDirectoriesFetcher
{
    public GitFetcher(URI uri)
    {
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
