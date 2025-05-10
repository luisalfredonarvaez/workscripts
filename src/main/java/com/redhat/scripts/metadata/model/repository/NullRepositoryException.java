package com.redhat.scripts.metadata.model.repository;

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
