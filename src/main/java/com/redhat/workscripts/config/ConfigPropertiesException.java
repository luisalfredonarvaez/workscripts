package com.redhat.workscripts.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigPropertiesException extends RuntimeException
{
    private String invalidProperty;
    private String formatMessage;

    public ConfigPropertiesException(String formatMessage, String invalidProperty)
    {
        super(String.format(formatMessage, invalidProperty));
        this.formatMessage = formatMessage;
        this.invalidProperty = invalidProperty;
    }
}
