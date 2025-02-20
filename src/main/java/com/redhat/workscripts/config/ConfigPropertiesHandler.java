//https://mkyong.com/spring-boot/spring-boot-configurationproperties-example/
package com.redhat.workscripts.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

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

    private PropertiesValidator propertiesValidator;


    public void validateProperties()
            throws ConfigPropertiesException
    {
        if (propertiesValidator == null)
            propertiesValidator = new PropertiesValidator(this);

        propertiesValidator.validateMenusFetchURIs();
        propertiesValidator.validateScriptEnvFilters();
    }

}
