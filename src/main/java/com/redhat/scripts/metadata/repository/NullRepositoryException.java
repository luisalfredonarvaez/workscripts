package com.redhat.scripts.metadata.repository;

public class NullRepositoryException extends RuntimeException
{
    public NullRepositoryException()
    {
        super();
    }
    public NullRepositoryException(String message)
    {
        super(message);
    }
}
