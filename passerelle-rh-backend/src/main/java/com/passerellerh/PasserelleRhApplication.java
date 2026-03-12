package com.passerellerh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PasserelleRhApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasserelleRhApplication.class, args);
    }

}
