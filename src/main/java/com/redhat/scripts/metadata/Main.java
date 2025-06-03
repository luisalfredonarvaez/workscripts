//https://www.baeldung.com/spring-boot-console-app
//https://github.com/mightychip/spring-boot-swing/blob/master/src/main/java/ca/purpleowl/examples/swing/SpringBootSwingApplication.java

package com.redhat.scripts.metadata;

import com.redhat.scripts.metadata.model.entities.Menu;
import com.redhat.scripts.metadata.view.gui.jswing.MenuGUI;
import com.redhat.scripts.metadata.app.services.ApplicationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.net.URISyntaxException;
import java.util.List;

@Log4j2
@SpringBootApplication
//@EntityScan(basePackageClasses = Directory.class)
//@EnableJpaRepositories(basePackageClasses = AbstractRepository.class)
public class Main implements CommandLineRunner
{
    @Autowired
    private ApplicationService applicationService;

    public static void main(String [] args)
    {
        log.info("Starting app");
//        SpringApplication.run(Main.class, args);

        new SpringApplicationBuilder(Main.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);

        log.info("App ended");
    }

    @Override
    public void run(String... args)
            throws URISyntaxException
    {
        Menu menu = applicationService.start();
        MenuGUI menuGUI = new MenuGUI(menu);
    }
}
