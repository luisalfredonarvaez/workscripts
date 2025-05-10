package com.redhat.scripts.metadata.model.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Metadata
{
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Setter(AccessLevel.NONE)
    private List<Tag> tagList;

    final private LocalDateTime created;
    private RunTarget runTarget;
    private Category category;
    private LocalDateTime lastUpdated;

    public Metadata()
    {
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.tagList = new ArrayList<>();
        this.category = null;
    }

    public void addTag(String title, String description)
    {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        this.tagList.add(new Tag(title, description));
        this.lastUpdated = LocalDateTime.now();
    }

    public void addTag(String title)
    {
        Objects.requireNonNull(title);
        this.tagList.add(new Tag(title));
        this.lastUpdated = LocalDateTime.now();
    }

    public void addTag(Tag tag)
    {
        Objects.requireNonNull(tag);
        this.tagList.add(tag);
        this.lastUpdated = LocalDateTime.now();
    }

    public void setCategory(Category category)
    {
        Objects.requireNonNull(category);
        this.category = category;
        this.lastUpdated = LocalDateTime.now();
    }

    public void setRunTarget(RunTarget runTarget)
    {
        Objects.requireNonNull(runTarget);
        this.runTarget = runTarget;
        this.lastUpdated = LocalDateTime.now();
    }
}
