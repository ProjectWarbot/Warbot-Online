package edu.warbot.online.config;

import edu.warbot.online.Application;
import edu.warbot.online.services.TeamService;
import edu.warbot.online.services.impl.TeamServiceImpl;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by beugnon on 05/05/15.
 *
 */

@Configuration
@ComponentScan(basePackageClasses = Application.class, excludeFilters = @ComponentScan.Filter({Service.class, Controller.class, Repository.class, Configuration.class}))
public class WarbotProcessConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("/persistence.properties"));
        return ppc;
    }

    @Bean
    public TeamService teamService() {
        return new TeamServiceImpl();
    }

}
