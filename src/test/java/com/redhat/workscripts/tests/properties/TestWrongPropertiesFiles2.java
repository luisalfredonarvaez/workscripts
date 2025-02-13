package com.redhat.workscripts.tests.properties;

//https://www.baeldung.com/spring-boot-testing-configurationproperties
//https://medium.com/@fmanaila/testing-validated-configurationproperties-with-applicationcontextrunner-4cb0d5a3a2d8

import com.redhat.workscripts.config.ConfigPropertiesException;
import com.redhat.workscripts.config.ConfigPropertiesHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWrongPropertiesFiles2
{
    @EnableConfigurationProperties(value = ConfigPropertiesHandler.class)
    static class TestConfiguration {
    }


    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    private ApplicationContextRunner contextRunner()
    {
        return new ApplicationContextRunner()
                .withUserConfiguration(TestConfiguration.class)
                .withPropertyValues("") // <-- empty properties, the context start should fail
                ;
    }

    @Test
    void givenValidConfigurationProperties_thenContextStarts() {
        contextRunner()
                .run((context) -> assertThat(context).hasFailed());
    }
}
