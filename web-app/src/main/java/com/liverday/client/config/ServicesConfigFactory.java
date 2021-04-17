package com.liverday.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties("oauth2.resource")
@Getter
@Setter
public class ServicesConfigFactory {
    public static String MICRO_SERVICE_FOO = "client-micro-service-foo";

    private Map<String, ServiceConfig> services;

    public ServiceConfig getServiceById(String serviceId) {
        return services
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(serviceId))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }
}
