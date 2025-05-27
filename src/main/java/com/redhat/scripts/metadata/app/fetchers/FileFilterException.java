package com.redhat.scripts.metadata.app.fetchers;

public class FileFilterException extends Exception
{
    public FileFilterException(String directoryPath, Throwable cause)
    {
        super("Could not filter files in directory: " + directoryPath, cause);
    }
}
