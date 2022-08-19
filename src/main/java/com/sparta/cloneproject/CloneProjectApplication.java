package com.sparta.cloneproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CloneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloneProjectApplication.class, args);
    }

}
