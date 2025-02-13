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
import java.util.List;

@Setter
@Getter
@Configuration
//application.properties is default PropertySource
//@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "menus.fetch")
@Validated
public class ConfigPropertiesHandler
{

    @NotEmpty
    private List<String> uris;

    public void validateProperties()
            throws ConfigPropertiesException
    {
        String propertyName = "menus.fetch.uris";
        if (null == uris || uris.size()==0)
            throw new ConfigPropertiesException("Invalid parameter array: '%s': Zero length or null value",
                    propertyName);

        for (int i=0; i<uris.size(); i++)
        {
            String uri = uris.get(i);
            if (!isValidURL(uri))
                throw new ConfigPropertiesException("Invalid array parameter: %s has not a valid value (%s) on index %d",
                        propertyName, uri, i);
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
}
