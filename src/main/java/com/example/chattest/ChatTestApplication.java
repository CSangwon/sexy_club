package com.example.chattest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatTestApplication.class, args);
    }

}
