//https://mkyong.com/spring-boot/spring-boot-configurationproperties-example/
package com.redhat.workscripts.config;

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
    private final String[] DEFAULT_SCRIPT_ENV_FILTERS = {"oc-setenv.sh", "setenv.sh", "env.sh"};

    //  Some default would be supplied, if the defaults dont work
//    @NotEmpty
    private List<String> scriptEnvFilters;

    //  The app may have no fetchers set up, because it starts with the DB info
//    @NotEmpty
    private List<String> menusFetchUris;

    //  Some default would be supplied
//    @NotEmpty
    private String repositoryType;

    private String appWorkdir;

    private PropertiesValidator propertiesValidator;

    private WorkDirectory workDirectory;

    private SupportedRepositoryType supportedRepositoryType;

    public void setupProperties()
            throws ConfigPropertiesException
    {
        setDefaultValues();
        workDirectory = new WorkDirectory(appWorkdir);

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

        supportedRepositoryType = setSupportedRepositoryTypeFromString(repositoryType);
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

        if (null == scriptEnvFilters || scriptEnvFilters.isEmpty())
        {
            log.debug("Setting 'scriptEnvFilters' to default values");
            scriptEnvFilters = Arrays.asList(DEFAULT_SCRIPT_ENV_FILTERS);
        }

        if (null == appWorkdir || appWorkdir.isEmpty())
        {
            log.debug("Setting 'appWorkdir' to default values");
            appWorkdir = WorkDirectory.getDefaultWorkdirFromOS();
        }

        if (null == repositoryType || repositoryType.isEmpty() || repositoryType.isBlank())
        {
            log.debug("Setting 'repositoryType' to default values");
            repositoryType = SupportedRepositoryType.FILE.label;
        }
    }
}
