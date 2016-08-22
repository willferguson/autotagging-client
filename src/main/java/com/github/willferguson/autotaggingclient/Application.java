package com.github.willferguson.autotaggingclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by will on 22/08/16.
 */

@SpringBootApplication
@EnableTask
public class Application {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings ->
                System.out.println("Executed at :" +
                        new SimpleDateFormat().format(new Date()));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
