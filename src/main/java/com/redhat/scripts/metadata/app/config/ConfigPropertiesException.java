package com.redhat.scripts.metadata.app.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ConfigPropertiesException extends IllegalArgumentException
{
    private String invalidPropertyName;
    private String invalidPropertyValue;
    private String formatMessage;
    private int index;

    public ConfigPropertiesException(@NonNull String message,
                                     @NonNull Throwable exception)
    {
        super(message, exception);
    }

    public ConfigPropertiesException(@NonNull String formatMessage,
                                     @NonNull String invalidPropertyName)
    {
        super(String.format(formatMessage, invalidPropertyName));
        this.setFormatMessage(formatMessage);
        this.setInvalidPropertyName(invalidPropertyName);
        this.setInvalidPropertyValue(null);
        this.setIndex(-1);
    }

    public ConfigPropertiesException(String formatMessage, String invalidPropertyName,
                                     String invalidPropertyValue)
    {
        super(String.format(formatMessage, invalidPropertyName, invalidPropertyValue));
        this.setFormatMessage(formatMessage);
        this.setInvalidPropertyName(invalidPropertyName);
        this.setInvalidPropertyValue(invalidPropertyValue);
        this.setIndex(-1);
    }

    public ConfigPropertiesException(String formatMessage, String invalidPropertyName,
                                     String invalidPropertyValue, int index)
    {
        super(String.format(formatMessage, invalidPropertyName, invalidPropertyValue, index));
        this.setFormatMessage(formatMessage);
        this.setInvalidPropertyName(invalidPropertyName);
        this.setInvalidPropertyValue(invalidPropertyValue);
        this.setIndex(index);
    }
}
