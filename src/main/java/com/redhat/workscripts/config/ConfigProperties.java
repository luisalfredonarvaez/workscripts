package com.redhat.workscripts.config;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

@Log4j2
public class ConfigProperties
{
    public static final String MANDATORY_PROPERTY_NOT_FOUND = "Mandatory property not found. Cannot continue: '%s'";
    private static final String DEFAULT_PROPERTIES_FILE_PATH = "src/main/resources/application.properties";
    private static final Logger log = LogManager.getLogger(ConfigProperties.class);
    private Properties commandLineProperties;
    private Properties fileProperties;
    private Properties validatedProperties;
    private final String[] mandatoryProperties =
            {
                    "menus.fetch.uri"
            };

    private ConfigProperties()
    {
        commandLineProperties = null;
        fileProperties = null;
        validatedProperties = new Properties();
    }

    private static ConfigProperties configPropertiesInstance;

    public static void initConfig(String[] commandLineArgs)
    {
        initConfig(commandLineArgs, DEFAULT_PROPERTIES_FILE_PATH);
    }


    public static void initConfig(String[] commandLineArgs, String pathPropertiesFile)
    {
        configPropertiesInstance = new ConfigProperties();
        configPropertiesInstance.commandLineProperties = ConfigPropertiesReaderFactory.createFromCommandLineArguments(commandLineArgs);
        configPropertiesInstance.fileProperties = ConfigPropertiesReaderFactory.createFromFile(pathPropertiesFile);
        configPropertiesInstance.validateProperties();
    }

    private void validateProperties()
    {
        Properties preValidatedProperties = new Properties();

        //Check if mandatory properties exists first
        checkMandatoryPropertiesExist(preValidatedProperties);
        log.debug("Mandatory properties validated");
    }

    private void checkMandatoryPropertiesExist(Properties preValidatedProperties)
    {
        //This will have the properties extracted from file and commandline, but

        for (String mandatoryProperty: mandatoryProperties)
        {
            log.debug("Validating mandatory property: " + mandatoryProperty);
            //command line properties overrides file properties
            if (commandLineProperties.containsKey(mandatoryProperty))
            {
                String value = commandLineProperties.getProperty(mandatoryProperty);
                preValidatedProperties.setProperty(mandatoryProperty, value);
            }
            else
            {
                if (fileProperties.containsKey(mandatoryProperty))
                {
                    String value = fileProperties.getProperty(mandatoryProperty);
                    preValidatedProperties.setProperty(mandatoryProperty, value);
                }
                else
                {
                    //Mandatory property not found in neither command line nor file properties
                    throw new RuntimeException("Mandatory property not found. Cannot continue: " + mandatoryProperty);
                }
            }
        }
    }
}
