package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import com.redhat.workscripts.entities.Directory;
import lombok.NonNull;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public abstract class DirectoriesFetcher
{
    protected ConfigPropertiesHandler configPropertiesHandler;
    protected URI uri;

    public DirectoriesFetcher(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.setConfigPropertiesHandler(configPropertiesHandler);
    }

    abstract public void refresh();
    abstract public List<Directory> fetchAllFromURI();

    protected void setConfigPropertiesHandler(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        this.configPropertiesHandler = configPropertiesHandler;
    }
}
