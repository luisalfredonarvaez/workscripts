package com.redhat.workscripts.fetchers;

import com.redhat.workscripts.entities.Directory;

public interface IDirectoriesFetcher
{
    void refresh();
    Directory fetchAllFromRootDirectory(String uri);
}
