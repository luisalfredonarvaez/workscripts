package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import lombok.NonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FetcherFactory
{
    private ConfigPropertiesHandler configPropertiesHandler;
    public FetcherFactory(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
    }

    public List<DirectoriesFetcher> getFetchers()
            throws URISyntaxException
    {
        return getFetchers(configPropertiesHandler.getMenusFetchUris());
    }

    public List<DirectoriesFetcher> getFetchers(@NonNull List<String> strUris)
            throws URISyntaxException
    {
        Objects.requireNonNull(strUris);

        List<DirectoriesFetcher> ret = new ArrayList<>();
        for (String strUri: strUris)
            ret.add(getFetcher(strUri));

        return ret;
    }

    public DirectoriesFetcher getFetcher(@NonNull String strUri)
            throws URISyntaxException
    {
        Objects.requireNonNull(strUri);

        DirectoriesFetcher directoriesFetcher;
        URI uri = new URI(strUri);

        String scheme = uri.getScheme();
        if (null == scheme)
            throw new IllegalArgumentException("suri has no scheme (new URI(suri).scheme == null)");

        if (scheme.equals("file"))
        {
            directoriesFetcher = new FileSystemDirectoriesFetcher(configPropertiesHandler, uri);
            return directoriesFetcher;
        }

        if (scheme.equals("http") || scheme.equals("git"))
        {
            directoriesFetcher = new GitFetcher(configPropertiesHandler, uri);
            return directoriesFetcher;
        }

        throw new UnsupportedOperationException("scheme still not implemented:" + scheme);
    }
}
