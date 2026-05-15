package com.reaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.reaction.mapper")
public class ReactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactionApplication.class, args);
    }
}