//https://mkyong.com/spring-boot/spring-boot-configurationproperties-example/
package com.redhat.workscripts.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Configuration
//application.properties is default PropertySource
//@PropertySource("classpath:application.properties")
@ConfigurationProperties
@Validated
public class ConfigPropertiesHandler
{

    @NotEmpty
    private List<String> menusFetchUris;

    @NotEmpty
    private List<String> scriptEnvFilters;


    public void validateProperties()
            throws ConfigPropertiesException
    {
        validateMenusFetchURIs();
        validateScriptEnvFilters();
    }

    private void validateMenusFetchURIs()
    {
        String propertyName="menusFetchUris";
        validateListFromParameter(menusFetchUris, propertyName);

        for (int i = 0; i< menusFetchUris.size(); i++)
        {
            String uri = menusFetchUris.get(i);
            if (!isValidURL(uri))
                throw new ConfigPropertiesException("Invalid array parameter: %s has not a valid value (%s) on index %d",
                        propertyName, uri, i);
        }
    }

    private void validateScriptEnvFilters()
    {
        String propertyName="scriptEnvFilters";
        validateListFromParameter(scriptEnvFilters, propertyName);

        for (int i = 0; i< scriptEnvFilters.size(); i++)
        {
            String fileName = scriptEnvFilters.get(i);
            if (!isValidFilename(fileName))
                throw new ConfigPropertiesException("Invalid array parameter: %s has not a valid value (%s) on index %d",
                        propertyName, fileName, i);
        }
    }

    public boolean isValidURL(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    //https://www.baeldung.com/java-validate-filename
    public boolean isValidFilename(String filename)
    {
        if (filename == null || filename.isEmpty() || filename.length() > 255) {
            return false;
        }
        return Arrays.stream(getInvalidCharsByOS())
                .noneMatch(ch -> filename.contains(ch.toString()));
    }

    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|'};
    public static final Character[] INVALID_UNIX_SPECIFIC_CHARS = {'\000','/'};
    public static Character[] getInvalidCharsByOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return INVALID_WINDOWS_SPECIFIC_CHARS;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return INVALID_UNIX_SPECIFIC_CHARS;
        } else {
            return new Character[]{};
        }
    }

    private void validateListFromParameter(List<?> list, String propertyName)
    {
        if (null == list || list.size()==0)
            throw new ConfigPropertiesException("Invalid parameter array: '%s': Zero length or null value",
                    propertyName);
    }
}
