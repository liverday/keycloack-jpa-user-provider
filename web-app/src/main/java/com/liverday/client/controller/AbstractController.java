package com.liverday.client.controller;

import com.liverday.client.config.ServiceConfig;
import com.liverday.client.config.ServicesConfigFactory;
import com.liverday.client.model.AbstractResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@AllArgsConstructor
public abstract class AbstractController {
    private final ServicesConfigFactory servicesConfigFactory;
    private final WebClient webClient;

    protected <T> AbstractResponse<T> callGet(
            String serviceId,
            OAuth2AuthorizedClient authorizedClient,
            Class<T> clazz
    ) throws Exception {
        return callGet(serviceId, authorizedClient, clazz, new LinkedMultiValueMap<>());
    }

    protected <T> AbstractResponse<T> callGet(
            String serviceId,
            OAuth2AuthorizedClient authorizedClient,
            Class<T> clazz,
            MultiValueMap<String, String> params
    ) throws Exception {
        String uriString = getUriFromServiceId(serviceId);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriString);

        if (!params.isEmpty()) {
            uriBuilder.queryParams(params);
        }

        URI uri = uriBuilder.build().toUri();

        try {
            T response = webClient
                    .get()
                    .uri(uri)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(clazz)
                    .block();
            return new AbstractResponse<>(true, response, null);
        } catch (Exception e) {
            return new AbstractResponse<>(false, null, e.getMessage());
        }
    }

    protected <T, B> AbstractResponse<T> callPost(
            String serviceId,
            OAuth2AuthorizedClient authorizedClient,
            B b,
            Class<T> clazz
    ) throws Exception {
        URI uri = new URI(getUriFromServiceId(serviceId));

        try {
            T response = webClient
                    .post()
                    .uri(uri)
                    .bodyValue(b)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(clazz)
                    .block();
            return new AbstractResponse<>(true, response, null);
        } catch (Exception e) {
            return new AbstractResponse<>(false, null, e.getMessage());
        }
    }

    private String getUriFromServiceId(String serviceId) throws Exception {
        ServiceConfig serviceConfig = servicesConfigFactory.getServiceById(serviceId);

        if (serviceConfig == null)
            throw new Exception("invalid service");

        return serviceConfig.getUri();
    }
}
