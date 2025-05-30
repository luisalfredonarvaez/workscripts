package com.redhat.scripts.metadata.tests.properties;

//https://www.baeldung.com/spring-boot-testing-configurationproperties

import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ConfigPropertiesHandler.class)
@TestPropertySource("classpath:test0-ok-application.properties")
public class TestGoodPropertiesFiles
{
    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    @Test
    void check()
    {
        configPropertiesHandler.setupProperties();
        configPropertiesHandler.validateProperties();

        assertEquals(configPropertiesHandler.getAppMenusFetchUris().size(), 2);
        assertEquals(configPropertiesHandler.getAppScriptWildcards().size(), 2);
    }
}
