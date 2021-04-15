package com.liverday.server.config;

import com.liverday.server.repositories.IFooRepository;
import com.liverday.server.repositories.implementations.InMemoryFooRepository;
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
