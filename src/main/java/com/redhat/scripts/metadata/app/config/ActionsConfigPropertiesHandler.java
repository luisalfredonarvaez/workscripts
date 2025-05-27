package com.redhat.scripts.metadata.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "action")
public class ActionsConfigPropertiesHandler
{
    public static final String DEFAULT_PACKAGE_NAME = "com.redhat.scripts.metadata.app.actions";
    @Autowired
    private Environment env;

    String packageName;

    public void loadActionsProperties()
    {
        if (null == packageName || packageName.isEmpty())
        {
            setPackageName(DEFAULT_PACKAGE_NAME);
        }


    }
}
