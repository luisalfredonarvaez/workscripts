package com.redhat.scripts.metadata.app.config;

import com.redhat.scripts.metadata.app.actions.InfoAction;
import com.redhat.scripts.metadata.app.actions.exceptions.ActionClassesNotFoundException;
import com.redhat.scripts.metadata.model.entities.MenuOptionAction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Component
@Log4j2
@ConfigurationProperties(prefix = "action")
@Configuration
@Validated
public class ActionsConfigPropertiesHandler
{
    public static final String DEFAULT_PACKAGE_NAME = "com.redhat.scripts.metadata.app.actions";

    private String packageName;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Set<Class<? extends MenuOptionAction>> actionClassesFound;

    @Setter(AccessLevel.NONE)
    private Set<InfoAction> infoActions;

    @Setter(AccessLevel.NONE)
    List<String> packageNames;


    public Set<InfoAction>
        loadActionsProperties() throws ActionClassesNotFoundException
    {
        if (null == packageName || packageName.isEmpty())
        {
            log.debug("property {} not detected, setting default package name: {}",
                    "action.packageName", DEFAULT_PACKAGE_NAME);

            setPackageName(DEFAULT_PACKAGE_NAME);
        }
        else
        {
            log.debug("Using package name from properties file: {}", packageName);
        }


        if (null == actionClassesFound)
        {
            log.debug("actionClassesFound is null, initializing it to an empty set");
            actionClassesFound = new HashSet<>();
        }
        else
        {
            log.debug("actionClassesFound is already initialized with {} classes",
                    actionClassesFound.size());
            log.debug("Clearing actionClassesFound set");
            actionClassesFound.clear();
        }

        //Ensure empty set for infoActions
        if (null == infoActions)
            infoActions = new HashSet<>();
        else
            infoActions.clear();


        for (String packageName : packageNames)
        {
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder()
                            .setUrls(ClasspathHelper.forPackage(packageName))
                            .setScanners(new SubTypesScanner(false))
                            .filterInputsBy(new FilterBuilder().includePackage(packageName))
            );

            Set<Class<? extends MenuOptionAction>> classes
                    = reflections.getSubTypesOf(MenuOptionAction.class);

            log.debug("Found classes in package {}: {}", packageName, classes.size());
            for (Class<?> clazz : classes) {
                log.debug("Found class: {}", clazz.getName());
            }

            actionClassesFound.addAll(classes);
        }

        if (actionClassesFound.isEmpty())
            throw new ActionClassesNotFoundException(getPackageNames());

        infoActions = generateClassInfo(actionClassesFound);
        return infoActions;
    }


    public void setPackageName(String packageName)
    {
        if (null == packageName || packageName.isEmpty())
        {
            throw new IllegalArgumentException("Package name cannot be null or empty");
        }
        this.packageName = packageName;
        this.packageNames = Arrays.stream(packageName.split(",")).toList();
    }

    private Set<InfoAction> generateClassInfo(Set<Class<? extends MenuOptionAction>> actionClasses)
    {
        Set<InfoAction> infoActions = new HashSet<>();
        for (Class<? extends MenuOptionAction> actionClass : actionClasses)
        {
            String actionName = actionClass.getSimpleName();
            infoActions.add(new InfoAction(actionClass, actionName));
        }
        return infoActions;
    }
}
