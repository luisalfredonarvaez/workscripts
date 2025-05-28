package com.redhat.scripts.metadata.tests.properties;

//https://www.baeldung.com/spring-boot-testing-configurationproperties

import com.redhat.scripts.metadata.app.actions.ActionParametrize;
import com.redhat.scripts.metadata.app.actions.InfoAction;
import com.redhat.scripts.metadata.app.config.ActionsConfigPropertiesHandler;
import com.redhat.scripts.metadata.app.config.ConfigPropertiesHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ActionsConfigPropertiesHandler.class)
@TestPropertySource("classpath:test0-ok-application.actions.properties")
public class TestGoodPropertiesFilesActions
{
    @Autowired
    private ActionsConfigPropertiesHandler actionsConfigPropertiesHandler;

    @Test
    void check()
    {
        Set<InfoAction> infoActions = actionsConfigPropertiesHandler.loadActionsProperties();

        InfoAction infoAction = new InfoAction(ActionParametrize.class, "ActionParametrize");
        assertTrue(infoActions.contains(infoAction));
    }
}
