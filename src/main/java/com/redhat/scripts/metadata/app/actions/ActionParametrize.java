package com.redhat.scripts.metadata.app.actions;

import com.redhat.scripts.metadata.model.entities.MenuOption;
import com.redhat.scripts.metadata.model.entities.MenuOptionAction;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO
/*
* Parametrize scripts detected
*
* */
@Log4j2
public final class ActionParametrize extends MenuOptionAction
{
    private final String[] DEFAULT_SCRIPT_ENV_FILTERS = {"oc-setenv.sh", "setenv.sh", "env.sh"};

    public ActionParametrize(MenuOption menuOption)
    {
        super(menuOption);
    }

    @Override
    public void execute()
    {
        //TODO
    }

    //https://www.baeldung.com/java-list-files-recursively
    private List<File> filterFilesAccordingToScriptFilter(File dir, List<String> scriptEnvFilters)
    {
        List<File> ret = new ArrayList<>();
        log.debug("property scriptEnvFilters -> ", scriptEnvFilters);

        Iterator<File> fileIterator = FileUtils.iterateFiles(dir, null, true);
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            String fileName = file.getName();
            log.debug("|{}|", fileName);
            if (!scriptEnvFilters.contains(fileName))
                log.debug("REJECTED File: {}", file.getAbsolutePath());
            else
            {
                log.debug("ACCEPTED File: {}", file.getAbsolutePath());
                ret.add(file);
            }
        }

        return ret;
    }
}
