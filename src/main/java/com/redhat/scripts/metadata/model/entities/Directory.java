package com.redhat.scripts.metadata.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Directory implements Comparable
{
    final private UUID id;
    final String diskLocationPath;
    /**
     * The disk location of the directory.
     * This is the absolute path to the directory on the disk.
     */
    final private File diskLocation;
    final private LocalDateTime created;
    private LocalDateTime lastUpdated;
    final private Metadata metadata;

    @Setter
    private UUID menuOptionId;

    public Directory(@JsonProperty("diskLocationPath") String diskLocationPath)
    {
        this(new File(diskLocationPath));
    }

    public Directory(File diskLocation)
    {
        Objects.requireNonNull(diskLocation);
        id = UUID.randomUUID();
        this.diskLocation = diskLocation;
        this.diskLocationPath = diskLocation.getAbsolutePath();
        this.menuOptionId = null;
        this.created = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.metadata = new Metadata();
    }

    @Override
    public boolean equals(Object o)
    {
        //TODO: This is not the best way to compare directories, but it works for now.
        if (this == o) return true;
        if (!(o instanceof Directory)) return false;
        Directory directory = (Directory) o;
        return this.id.equals(directory.id) && this.diskLocation.equals(directory.diskLocation);
    }

    @Override
    public int compareTo(Object o)
    {
        if (!(o instanceof Directory))
            throw new RuntimeException("Programming error: Passed object is not a Directory");

        if (this == o) return 0;

        Directory directory = (Directory) o;
        return this.diskLocation.compareTo(directory.diskLocation);
    }

    public void setMetadataRunTarget(RunTarget runTarget)
    {
        Objects.requireNonNull(runTarget);
        this.metadata.setRunTarget(runTarget);
        this.lastUpdated = LocalDateTime.now();
    }

    public void setMetadataCategory(Category category)
    {
        Objects.requireNonNull(category);
        this.metadata.setCategory(category);
        this.lastUpdated = LocalDateTime.now();
    }

    public void addMetadataTag(String title, String description)
    {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        this.metadata.addTag(title, description);
        this.lastUpdated = LocalDateTime.now();
    }

    public void addMetadataTag(String title)
    {
        Objects.requireNonNull(title);
        this.metadata.addTag(title);
        this.lastUpdated = LocalDateTime.now();
    }

    public void addMetadataTag(Tag tag)
    {
        Objects.requireNonNull(tag);
        this.metadata.addTag(tag);
        this.lastUpdated = LocalDateTime.now();
    }
}