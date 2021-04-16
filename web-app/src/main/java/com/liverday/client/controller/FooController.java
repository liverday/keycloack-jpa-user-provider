package com.liverday.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@Controller
public class FooController {
    Logger logger = LoggerFactory.getLogger(FooController.class);

    @GetMapping("/")
    public String index(Model model, OAuth2AuthenticationToken token) {
        return "index";
    }
}
