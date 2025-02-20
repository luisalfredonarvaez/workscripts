package com.redhat.workscripts.fetchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FetcherFactory
{
    public List<AbstractDirectoriesFetcher> getFetchers(List<String> suris)
            throws URISyntaxException
    {
        Objects.requireNonNull(suris);

        List<AbstractDirectoriesFetcher> ret = new ArrayList<>();
        for (String suri: suris)
            ret.add(getFetcher(suri));

        return ret;
    }

    public AbstractDirectoriesFetcher getFetcher(String suri)
            throws URISyntaxException
    {
        //TODO
        AbstractDirectoriesFetcher iDirectoriesFetcher = null;
        URI uri = new URI(suri);

        String scheme = uri.getScheme();
        if (null == scheme)
            throw new IllegalArgumentException("suri has no scheme (new URI(suri).scheme == null)");

        if (scheme.equals("file"))
        {
            iDirectoriesFetcher = new FileSystemFetcher(uri);
            return iDirectoriesFetcher;
        }

        if (scheme.equals("http") || scheme.equals("git"))
        {
            iDirectoriesFetcher = new GitFetcher(uri);
            return iDirectoriesFetcher;
        }

        throw new UnsupportedOperationException("scheme still not implemented:" + scheme);
    }
}
