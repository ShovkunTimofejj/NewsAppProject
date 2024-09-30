package com.example.instanews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // Indicates that this is a Spring Boot application
@EnableScheduling // Enables scheduling support in the application
public class SpringBootJavaFxApp {
    public static void main(String[] args) {
        // Launching the Spring Boot application
        SpringApplication.run(SpringBootJavaFxApp.class, args);

        // Launching the JavaFX application
        MainApp.launchApp(MainApp.class, args);
    }
}
