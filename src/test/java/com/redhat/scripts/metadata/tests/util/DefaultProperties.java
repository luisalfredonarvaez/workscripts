package com.redhat.scripts.metadata.tests.util;

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;

import java.util.Arrays;

public class DefaultProperties
{
    public static ConfigPropertiesHandler testDefaultConfigPropertiesHandler()
    {
        ConfigPropertiesHandler configPropertiesHandler = new ConfigPropertiesHandler();
        String[] appMenusFetchUris = {
                "file:///home/anarvaez/files/redhat/projects/workscripts-test-directory/_scripts_menu/"
        };
        configPropertiesHandler.setAppMenusFetchUris(Arrays.asList(appMenusFetchUris));
        configPropertiesHandler.setupProperties();
        configPropertiesHandler.validateProperties();

        return configPropertiesHandler;
    }
}
