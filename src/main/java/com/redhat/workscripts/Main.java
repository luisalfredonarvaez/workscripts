//https://www.baeldung.com/spring-boot-console-app
package com.redhat.workscripts;

import com.redhat.workscripts.config.ConfigPropertiesHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class Main implements CommandLineRunner
{
    @Autowired
    private ConfigPropertiesHandler configPropertiesHandler;

    public static void main(String [] args)
    {
        log.info("Starting app");
        SpringApplication.run(Main.class, args);
        log.info("App ended");
    }

    @Override
    public void run(String... args)
    {
        configPropertiesHandler.validateProperties();
        log.info("Config loaded");
    }
}
