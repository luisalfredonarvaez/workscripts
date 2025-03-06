package com.redhat.workscripts.repository;

public class NullRepositoryExecutorException extends RuntimeException
{
    public NullRepositoryExecutorException()
    {
        super();
    }
    public NullRepositoryExecutorException(String message)
    {
        super(message);
    }
}
