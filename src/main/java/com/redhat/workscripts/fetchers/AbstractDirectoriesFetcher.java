package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.entities.Directory;

import java.net.URI;
import java.util.List;

public abstract class AbstractDirectoriesFetcher
{
    protected URI uri;
    abstract public void refresh();
    abstract public List<Directory> fetchAllFromURI();
}
