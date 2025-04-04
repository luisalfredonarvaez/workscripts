//https://www.baeldung.com/spring-boot-console-app
package com.redhat.scripts.metadata;

import com.redhat.scripts.metadata.config.ConfigPropertiesHandler;
import com.redhat.scripts.metadata.services.ApplicationProcessesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;

@Log4j2
@SpringBootApplication
//@EntityScan(basePackageClasses = Directory.class)
//@EnableJpaRepositories(basePackageClasses = AbstractRepository.class)
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
    public void run(String... args) throws URISyntaxException
    {
        ApplicationProcessesService applicationProcessesService = new ApplicationProcessesService(configPropertiesHandler);
        applicationProcessesService.init();
    }
}
