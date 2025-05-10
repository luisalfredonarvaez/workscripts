package com.redhat.scripts.metadata.app.fetchers;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.model.entities.Directory;
import lombok.Getter;
import lombok.NonNull;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class DirectoriesFetcher
{
    protected ConfigPropertiesHandler configPropertiesHandler;
    protected URI uri;
    protected List<Directory> lastFetchedDirectories;

    public DirectoriesFetcher(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.setConfigPropertiesHandler(configPropertiesHandler);
    }

    abstract public void refresh();
    abstract protected List<Directory> fetchAllFromURIImpl();

    public List<Directory> fetchAllFromURI()
    {
        this.lastFetchedDirectories = this.fetchAllFromURIImpl();
        return this.lastFetchedDirectories;
    }

    protected void setConfigPropertiesHandler(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        this.configPropertiesHandler = configPropertiesHandler;
    }
}
