package com.redhat.workscripts;

import com.redhat.workscripts.config.ConfigProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
public class Main
{
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String [] args)
    {
        log.info("Starting app");
        ConfigProperties.initConfig(args);
        log.info("Config loaded");
    }
}
