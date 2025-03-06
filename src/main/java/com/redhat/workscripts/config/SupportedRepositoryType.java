package com.redhat.workscripts.config;

import java.util.Optional;

public enum SupportedRepositoryType
{
    FILE("file");

    public final String label;

    private SupportedRepositoryType(String label) {
        this.label = label;
    }

    public static Optional<SupportedRepositoryType> lookForEquivalent(String repositoryType)
    {
        for (SupportedRepositoryType srt: SupportedRepositoryType.values())
        {
            if (srt.label.equalsIgnoreCase(repositoryType))
                return Optional.of(srt);
        }

        return Optional.empty();
    }
}
