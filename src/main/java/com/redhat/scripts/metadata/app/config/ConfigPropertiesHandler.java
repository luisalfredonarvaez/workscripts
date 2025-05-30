//https://mkyong.com/spring-boot/spring-boot-configurationproperties-example/
package com.redhat.scripts.metadata.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;


@Setter
@Getter
@Configuration
//application.properties is default PropertySource
//@PropertySource("classpath:application.properties")
@ConfigurationProperties
@Validated
@Log4j2
public class ConfigPropertiesHandler
{
    private final String[] DEFAULT_SCRIPT_FILTERS = {"*.sh"};

    //  Some default would be supplied, if the defaults doesn't work
//    @NotEmpty
    private List<String> appScriptWildcards;

    //  The app may have no fetchers set up, because it starts with the DB info
//    @NotEmpty
    private List<String> appMenusFetchUris;

    //  Some default would be supplied
//    @NotEmpty
    private String appRepositoryType;

    private String appPathWorkdir;

    private PropertiesValidator propertiesValidator;

    private WorkDirectory workDirectory;

    private SupportedRepositoryType supportedRepositoryType;

    public void setupProperties()
            throws ConfigPropertiesException
    {
        setDefaultValues();
        workDirectory = new WorkDirectory(appPathWorkdir);

        if (!workDirectory.exists())
        {
            //The directory has never been created, lets create it!
            try
            {
                workDirectory.create();
            }
            catch (IOException ioException)
            {
                throw new ConfigPropertiesException("Could not create directory specified in property '%s' with value '%s'", "appWorkdir",
                        workDirectory.getWorkdirAsString());
            }

            //Lets check if the directory is writable
            try
            {
                if (!workDirectory.canWrite())
                    throw new ConfigPropertiesException("Could not create directory specified in property '%s' with value '%s'", "appWorkdir",
                            workDirectory.getWorkdirAsString());
            }
            catch (SecurityException securityException)
            {
                throw new ConfigPropertiesException("Security exception happened", securityException);
            }

        }
    }

    public void validateProperties()
            throws ConfigPropertiesException
    {
        if (propertiesValidator == null)
            propertiesValidator = new PropertiesValidator(this);

        propertiesValidator.validateMenusFetchURIs();
        propertiesValidator.validateScriptEnvFilters();
        propertiesValidator.validateWorkDirectory();
        propertiesValidator.validateRepositoryType();

        supportedRepositoryType = setSupportedRepositoryTypeFromString(appRepositoryType);
    }

    private SupportedRepositoryType setSupportedRepositoryTypeFromString(String repositoryType)
    {
        Optional<SupportedRepositoryType> optionalSupportedRepositoryType =
                SupportedRepositoryType.lookForEquivalent(repositoryType);

        //It has been validated before! should not be null
        if (optionalSupportedRepositoryType.isEmpty())
            throw new RuntimeException("Programming error");

        setSupportedRepositoryType(optionalSupportedRepositoryType.get());
        return getSupportedRepositoryType();
    }

    public void setDefaultValues()
    {
        log.debug("Setting default values for properties!");

        if (null == appScriptWildcards || appScriptWildcards.isEmpty())
        {
            log.debug("Setting 'appScriptFilters' to default values");
            appScriptWildcards = Arrays.asList(DEFAULT_SCRIPT_FILTERS);
        }

        if (null == appPathWorkdir || appPathWorkdir.isEmpty())
        {
            log.debug("Setting 'appWorkdir' to default values");
            appPathWorkdir = WorkDirectory.getDefaultWorkdirFromOS();
        }

        if (null == appRepositoryType || appRepositoryType.isEmpty() || appRepositoryType.isBlank())
        {
            log.debug("Setting 'repositoryType' to default values");
            appRepositoryType = SupportedRepositoryType.FILE.label;
        }
    }
}
