package edu.warbot.online.cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by beugnon on 11/06/15.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ClusterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusterApplication.class);
    }
}
