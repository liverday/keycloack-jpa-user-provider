package com.liverday.client.controller;

import com.liverday.client.config.ServicesConfigFactory;
import com.liverday.client.model.AbstractResponse;
import com.liverday.client.model.Foo;
import com.liverday.client.model.dto.CreateFooDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@SuppressWarnings("unused")
public class FooController extends AbstractController {
    Logger logger = LoggerFactory.getLogger(FooController.class);

    public FooController(ServicesConfigFactory servicesConfigFactory, WebClient webClient) {
        super(servicesConfigFactory, webClient);
    }

    @GetMapping("/")
    public String index(
            Model model,
            @RegisteredOAuth2AuthorizedClient("client-micro-service-foo") OAuth2AuthorizedClient authorizedClient,
            OAuth2AuthenticationToken authenticationToken
    ) throws Exception {
        model.addAttribute("userName", authenticationToken.getName());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());

        AbstractResponse<Foo[]> response = callGet(ServicesConfigFactory.MICRO_SERVICE_FOO, authorizedClient, Foo[].class);

        if (response.isSuccess()) {
            model.addAttribute("foos", Arrays.asList(response.getResponseBody()));
        }

        return "index";
    }

    @PostMapping("/foo")
    public ModelAndView create(
            @Valid @ModelAttribute("createFooDTO") CreateFooDTO createFooDTO,
            @RegisteredOAuth2AuthorizedClient("client-micro-service-foo") OAuth2AuthorizedClient authorizedClient
    ) throws Exception {
        AbstractResponse<Foo> response = callPost(
                ServicesConfigFactory.MICRO_SERVICE_FOO,
                authorizedClient,
                createFooDTO,
                Foo.class
        );
        return new ModelAndView("redirect:/");
    }
}
