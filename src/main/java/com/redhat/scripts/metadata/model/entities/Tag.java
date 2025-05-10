package com.redhat.scripts.metadata.model.entities;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Tag
{
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created;

    public Tag(@NonNull String title)
    {
        Objects.requireNonNull(title);
        this.title = title;
        this.created = LocalDateTime.now();
    }

    public Tag(@NonNull String title, @NonNull String description)
    {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        this.title = title;
        this.description = description;
        this.created = LocalDateTime.now();
    }
}
