package com.redhat.scripts.metadata.tests.properties;

//https://www.baeldung.com/spring-boot-testing-configurationproperties
//https://medium.com/@fmanaila/testing-validated-configurationproperties-with-applicationcontextrunner-4cb0d5a3a2d8

/*
*
* Testing empty app properties
* The empty properties are set in the test directly, no need for an empty properties file
*
* */


import com.redhat.scripts.metadata.app.config.ConfigPropertiesException;
import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.app.config.PropertiesValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ConfigPropertiesHandler.class)
@TestPropertySource("classpath:test0-wrong2-application.properties")
public class TestWrongPropertiesFiles2
{
    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    @Test
    void check()
    {
        try
        {
            configPropertiesHandler.setupProperties();
            configPropertiesHandler.validateProperties();
            //It this point is reached, no exception is thrown, which is bad because we expect an exception
            assert(false);
        }
        catch (ConfigPropertiesException cpe)
        {
            assertEquals(PropertiesValidator.MENUS_FETCH_URIS_PROPERTY_NAME, cpe.getInvalidPropertyName());
        }
        assert(true);
    }
}
