package com.liverday.microservice.foo.config;

import com.liverday.microservice.foo.repositories.IFooRepository;
import com.liverday.microservice.foo.repositories.implementations.InMemoryFooRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("unused")
public class RepositoryConfig {

    @Bean
    public IFooRepository fooRepository() {
        return new InMemoryFooRepository();
    }
}
