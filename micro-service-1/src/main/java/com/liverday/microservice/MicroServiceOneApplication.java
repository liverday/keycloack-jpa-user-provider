package com.liverday.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.liverday.microservice.foo")
public class MicroServiceOneApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroServiceOneApplication.class, args);
    }
}
