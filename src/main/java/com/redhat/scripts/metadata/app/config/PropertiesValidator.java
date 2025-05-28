package com.redhat.scripts.metadata.app.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PropertiesValidator
{
    public static final String MENUS_FETCH_URIS_PROPERTY_NAME = "appMenusFetchUris";
    public static final String SCRIPT_WILDCARDS_PROPERTY_NAME = "appScriptWildcards";

    private final ConfigPropertiesHandler configPropertiesHandler;
    public PropertiesValidator(@NonNull ConfigPropertiesHandler configPropertiesHandler)
    {
        Objects.requireNonNull(configPropertiesHandler);
        this.configPropertiesHandler = configPropertiesHandler;
    }

    public void validateMenusFetchURIs()
    {
        String propertyName=MENUS_FETCH_URIS_PROPERTY_NAME;
        @NotEmpty List<String> menusFetchUris = configPropertiesHandler.getAppMenusFetchUris();
        validateListFromParameter(menusFetchUris, propertyName);

        for (int i = 0; i< menusFetchUris.size(); i++)
        {
            String uri = menusFetchUris.get(i);
            if (!isValidURL(uri))
                throw new ConfigPropertiesException("Invalid array parameter: %s has not a valid value (%s) on index %d",
                        propertyName, uri, i);
        }
    }

    public void validateScriptEnvFilters()
    {
        String propertyName=SCRIPT_WILDCARDS_PROPERTY_NAME;
        @NotEmpty List<String> scriptEnvFilters = configPropertiesHandler.getAppScriptWildcards();

        validateListFromParameter(scriptEnvFilters, propertyName);

        for (int i = 0; i< scriptEnvFilters.size(); i++)
        {
            String fileName = scriptEnvFilters.get(i);
            if (!isValidFilename(fileName))
                throw new ConfigPropertiesException("Invalid array parameter: %s has not a valid value (%s) on index %d",
                        propertyName, fileName, i);
        }
    }

    private boolean isValidURL(@NonNull String url)
    {
        Objects.requireNonNull(url);

        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    //https://www.baeldung.com/java-validate-filename
    private boolean isValidFilename(@NonNull String filename)
    {
        Objects.requireNonNull(filename);

        if (filename.isEmpty() || filename.length() > 255) {
            return false;
        }
        return Arrays.stream(getInvalidCharsByOS())
                .noneMatch(ch -> filename.contains(ch.toString()));
    }

    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|'};
    public static final Character[] INVALID_UNIX_SPECIFIC_CHARS = {'\000','/'};
    public static Character[] getInvalidCharsByOS()
    {
        SupportedOS supportedOS = OSUtil.getSupportedOS();

        if (supportedOS == SupportedOS.WIN)
            return INVALID_WINDOWS_SPECIFIC_CHARS;
        if (supportedOS == SupportedOS.UNIX_LINUX || supportedOS == SupportedOS.MACOS)
            return INVALID_UNIX_SPECIFIC_CHARS;

        throw new RuntimeException("Programming error");
    }

    private void validateListFromParameter(List<?> list, String propertyName)
    {
        Objects.requireNonNull(propertyName);

        if (null == list || list.isEmpty())
            throw new ConfigPropertiesException("Invalid parameter array: '%s': Zero length or null value",
                    propertyName);
    }

    public void validateWorkDirectory()
    {
        WorkDirectory workDirectory = configPropertiesHandler.getWorkDirectory();
        Objects.requireNonNull(workDirectory);

        if (!workDirectory.isValidWorkDir())
            throw new ConfigPropertiesException("Invalid application workdir parameter '%s': '%s': Not a valid directory" +
                    " or not readable or does not exist",
                    "appWorkdir", workDirectory.getWorkdirAsString());
    }

    public void validateRepositoryType()
    {
        String repositoryType = configPropertiesHandler.getAppRepositoryType();
        Objects.requireNonNull(repositoryType);


        Optional<SupportedRepositoryType> opSRT = SupportedRepositoryType.lookForEquivalent(repositoryType);
        boolean validValue=opSRT.isPresent();


        if (!validValue)
            throw new ConfigPropertiesException("Invalid repository type '%s'",
                    "repositoryType", repositoryType);
    }
}
