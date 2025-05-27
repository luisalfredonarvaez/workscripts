package com.redhat.scripts.metadata.app.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Setter
@Getter
@Log4j2
public class WorkDirectory
{
    public static final String DEFAULT_WORKDIR_RELATIVE_FOLDER_NAME = "workscripts";
    private File workdirAsFile;
    private String workdirAsString;

    public WorkDirectory(@NonNull String appWorkdir)
    {
        if (null == appWorkdir || appWorkdir.isEmpty())
        {
           this.workdirAsString =  getDefaultWorkdirFromOS();
           log.debug("Setting appdir by default: {}", appWorkdir);
        }
        else
            this.workdirAsString = appWorkdir;

        workdirAsFile = new File(this.workdirAsString);
    }

    public boolean exists()
    {
        return workdirAsFile.exists();
    }

    public boolean canWrite()
            throws SecurityException
    {
        return workdirAsFile.canWrite();
    }

    public void create() throws IOException
    {
        try
        {
            boolean couldCreate = workdirAsFile.mkdir();
            log.debug("workdirAsFile.createNewFile() = {}", couldCreate);
            if (!couldCreate)
                throw new IOException("workdirAsFile.createNewFile() == false");
        }
        catch (IOException ioException)
        {
            throw new IOException("Could not create work directory", ioException);
        }
    }

    public boolean isValidWorkDir()
    {
        return workdirAsFile.isDirectory() && workdirAsFile.exists() && workdirAsFile.canWrite();
    }

    //https://www.google.com/search?q=desktop+app+best+practices+work+directory&sca_esv=be29cc62fc8e0244&sxsrf=AHTn8zoqPcxG3iy9NoEjs9TiRENDNmH01w%3A1740148810593&ei=SpC4Z-TyI7eTwbkPvbDAiAs&ved=0ahUKEwjkv9i9_9SLAxW3STABHT0YELEQ4dUDCBI&uact=5&oq=desktop+app+best+practices+work+directory&gs_lp=Egxnd3Mtd2l6LXNlcnAiKWRlc2t0b3AgYXBwIGJlc3QgcHJhY3RpY2VzIHdvcmsgZGlyZWN0b3J5MgQQIxgnMgUQABjvBTIFEAAY7wUyBRAAGO8FMgUQABjvBTIFEAAY7wVIhgxQvwpYvwpwAXgBkAEAmAGIAaABiAGqAQMwLjG4AQPIAQD4AQGYAgKgAo8BwgIKEAAYsAMY1gQYR5gDAIgGAZAGCJIHAzEuMaAH2gU&sclient=gws-wiz-serp
    public static String getDefaultWorkdirFromOS(@NonNull SupportedOS supportedOS)
    {
        Objects.requireNonNull(supportedOS);
        if (supportedOS == SupportedOS.WIN)
        {
            String appData = OSUtil.windowsGetAppDir();
            if (null == appData || appData.isEmpty())
                throw new RuntimeException("Got empty %APPDATA% for default workdir on Windows. Cannot continue");

            return new StringBuilder().append(appData).append(File.separator)
                    .append(DEFAULT_WORKDIR_RELATIVE_FOLDER_NAME).toString();
        }

        if (supportedOS == SupportedOS.UNIX_LINUX)
        {
            String userHome = OSUtil.linuxGetUserHome();
            if (null == userHome || userHome.isEmpty())
                throw new RuntimeException("Got empty $HOME for default workdir on Linux/Unix. Cannot continue");

            String configDir = ".config";

            return new StringBuilder().append(userHome).append(File.separator).append(configDir)
                    .append(File.separator).append(DEFAULT_WORKDIR_RELATIVE_FOLDER_NAME).toString();
        }

        if (supportedOS == SupportedOS.MACOS)
        {
            String userHome = OSUtil.macGetUserHome();
            if (null == userHome || userHome.isEmpty())
                throw new RuntimeException("Got empty $HOME for default workdir on Mac. Cannot continue");

            return new StringBuilder().append(userHome).append(File.separator)
                    .append("Library").append(File.separator)
                    .append("Application Support").append(File.separator)
                    .append(DEFAULT_WORKDIR_RELATIVE_FOLDER_NAME).toString();
        }

        throw new RuntimeException("Programming error!");
    }

    public static String getDefaultWorkdirFromOS()
    {
        SupportedOS supportedOS = OSUtil.getSupportedOS();
        return getDefaultWorkdirFromOS(supportedOS);

    }
}
