package com.redhat.scripts.metadata.model.services;

import com.redhat.scripts.metadata.model.entities.Directory;
import com.redhat.scripts.metadata.model.entities.Menu;
import com.redhat.scripts.metadata.app.fetchers.DirectoriesFetcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService
{
    public Menu buildMenuFromDirectoryFetchers(List<DirectoriesFetcher> directoryFetchersList)
    {
        List<Directory> directoryList = new ArrayList<>();

        for (DirectoriesFetcher directoryFetcher : directoryFetchersList)
        {
            List<Directory> directories = directoryFetcher.getLastFetchedDirectories();
            if (directories != null)
            {
                directoryList.addAll(directories);
            }
        }

        List<Directory> uniqueDirectories = directoryList.stream()
                .sorted()
                .distinct()
                .toList();

        Menu menu = new Menu(uniqueDirectories);
        return menu;
    }
}
