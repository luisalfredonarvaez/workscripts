package com.redhat.workscripts.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigPropertiesException extends IllegalArgumentException
{
    private String invalidPropertyName;
    private String invalidPropertyValue;
    private String formatMessage;
    private int index;

    public ConfigPropertiesException(String formatMessage,
                                     String invalidPropertyName)
    {
        super(String.format(formatMessage, invalidPropertyName));
        this.formatMessage = formatMessage;
        this.invalidPropertyName = invalidPropertyName;
        this.invalidPropertyValue = null;
        this.index = -1;
    }

    public ConfigPropertiesException(String formatMessage, String invalidPropertyName,
                                     String invalidPropertyValue)
    {
        super(String.format(formatMessage, invalidPropertyName, invalidPropertyValue));
        this.formatMessage = formatMessage;
        this.invalidPropertyName = invalidPropertyName;
        this.invalidPropertyValue = invalidPropertyValue;
        this.index = -1;
    }

    public ConfigPropertiesException(String formatMessage, String invalidPropertyName,
                                     String invalidPropertyValue, int index)
    {
        super(String.format(formatMessage, invalidPropertyName, invalidPropertyValue, index));
        this.formatMessage = formatMessage;
        this.invalidPropertyName = invalidPropertyName;
        this.invalidPropertyValue = invalidPropertyValue;
        this.index = index;
    }
}
