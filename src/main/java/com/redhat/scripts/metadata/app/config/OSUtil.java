package com.redhat.scripts.metadata.app.config;

public class OSUtil
{
    private static SupportedOS supportedOS;
    public static SupportedOS getSupportedOS()
    {
        if (supportedOS != null)
            return supportedOS;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
        {
            supportedOS = SupportedOS.WIN;
            return SupportedOS.WIN;
        }

        if (os.contains("nix") || os.contains("nux"))
        {
            supportedOS = SupportedOS.UNIX_LINUX;
            return SupportedOS.UNIX_LINUX;
        }

        if (os.contains("mac"))
        {
            supportedOS = SupportedOS.MACOS;
            return SupportedOS.MACOS;
        }

        throw new RuntimeException("Unsupported OS exception");
    }

    public static String windowsGetAppDir()
    {
        return System.getenv("APPDATA");
    }

    public static String linuxGetUserHome()
    {
        return System.getenv("HOME");
    }

    public static String macGetUserHome()
    {
        return System.getenv("HOME");
    }
}
