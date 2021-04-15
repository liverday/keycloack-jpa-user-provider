package com.liverday.server.controller;

import com.liverday.server.model.Foo;
import com.liverday.server.service.FooService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foos")
@SuppressWarnings("unused")
public class FooController {
    Logger logger = LoggerFactory.getLogger(FooController.class);

    @Autowired
    private FooService fooService;

    @GetMapping("/{id}")
    public Foo findById(@PathVariable String id, BearerTokenAuthentication authentication) {
        logger.info("findById({}, {})", id, authentication);
        return new Foo();
    }
}
