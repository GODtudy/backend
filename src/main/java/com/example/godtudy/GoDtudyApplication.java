package com.example.godtudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoDtudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoDtudyApplication.class, args);
    }

}
