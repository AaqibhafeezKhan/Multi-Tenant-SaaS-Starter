package com.saas.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SaasCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaasCoreApplication.class, args);
    }
}
