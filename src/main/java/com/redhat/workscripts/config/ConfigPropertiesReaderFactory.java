package com.redhat.workscripts.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigPropertiesReaderFactory
{

    public static final String STRING_IS_NOT_A_VALID_PROPERTY_CANNOT_CONTINUE = "String is not a valid property, cannot continue: '%s'";

    public static Properties createFromFile(String fileName)
    {
        Objects.requireNonNull(fileName);

        Properties properties;

        try
        {
            File file = new File(fileName);
            if (!file.exists())
                throw new FileNotFoundException();

            properties = new Properties();
            properties.load(new FileReader(file));
            return properties;
        }
        catch (FileNotFoundException fnfe)
        {
            throw new RuntimeException("Cannot find " + fileName + ". Cannot continue", fnfe);
        }
        catch (IOException ioe)
        {
            throw new RuntimeException("Cannot load properties from " + fileName + ". Cannot continue", ioe);
        }

    }

    //TODO
    public static Properties createFromCommandLineArguments(String[] args)
    {
        Objects.requireNonNull(args);

        Properties properties = new Properties();

        for (String arg: args)
        {
            String[] nameValue = parseNameValueFromString(arg);
            if (null == nameValue || nameValue.length != 2)
                throwArgStringInvalidProperty(arg);

            properties.setProperty(nameValue[0], nameValue[1]);
        }

        return properties;
    }

    private static String[] parseNameValueFromString(String arg)
    {
        if (null == arg || arg.isEmpty())
            return null;

        if (!arg.contains("="))
            return null;

        int indexEqual = arg.indexOf('=');

        if (indexEqual == -1)
            return null;

        String[] ret = new String[2];

        //property name
        ret[0] = arg.substring(0, indexEqual);
        //property value
        ret[1] = arg.substring(indexEqual+1);

        return ret;
    }

    private static void throwArgStringInvalidProperty(String arg)
    {
        Objects.requireNonNull(arg);

        ConfigPropertiesException cpe =
                new ConfigPropertiesException(STRING_IS_NOT_A_VALID_PROPERTY_CANNOT_CONTINUE, arg);

        throw cpe;
    }
}
