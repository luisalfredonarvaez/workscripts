package com.redhat.scripts.metadata.tests.properties;

//https://www.baeldung.com/spring-boot-testing-configurationproperties

import com.redhat.scripts.metadata.app.actions.exceptions.ActionClassesNotFoundException;
import com.redhat.scripts.metadata.app.config.ActionsConfigPropertiesHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ActionsConfigPropertiesHandler.class)
@TestPropertySource("classpath:test0-wrong4-application.properties")
public class TestWrongPropertiesFiles4Actions
{
    @Autowired
    private ActionsConfigPropertiesHandler actionsConfigPropertiesHandler;

    @Test
    void check()
    {
        try
        {
            actionsConfigPropertiesHandler.loadActionsProperties();
            //It this point is reached, no exception is thrown, which is bad because we expect an exception
            assert(false);
        }
        catch (ActionClassesNotFoundException ex)
        {
            assert(true);
        }
        assert(true);
    }
}
