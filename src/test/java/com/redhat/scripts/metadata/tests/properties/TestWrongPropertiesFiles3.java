package com.redhat.scripts.metadata.tests.properties;

//https://www.baeldung.com/spring-boot-testing-configurationproperties

import com.redhat.scripts.metadata.config.ConfigPropertiesException;
import com.redhat.scripts.metadata.config.ConfigPropertiesHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ConfigPropertiesHandler.class)
@TestPropertySource("classpath:test0-wrong3-application.properties")
public class TestWrongPropertiesFiles3
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
            assertEquals("scriptEnvFilters", cpe.getInvalidPropertyName() );
            assertEquals(0, cpe.getIndex());
        }
        assert(true);
    }
}
