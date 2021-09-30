package com.talkkia.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TalkkiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkkiaApplication.class, args);
    }

}
