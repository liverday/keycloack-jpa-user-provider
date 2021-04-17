package com.liverday.microservice.foo.controller;

import com.liverday.microservice.foo.model.Foo;
import com.liverday.microservice.foo.model.dto.CreateFooDTO;
import com.liverday.microservice.foo.service.FooService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SuppressWarnings("unused")
public class FooController {
    Logger logger = LoggerFactory.getLogger(FooController.class);

    @Autowired
    private FooService fooService;

    @GetMapping("/foos")
    public List<Foo> findAll(JwtAuthenticationToken authentication) {
        String userId = authentication.getToken().getSubject();
        return fooService.findAll(userId);
    }

    @GetMapping("/foos/{id}")
    public Foo findById(@PathVariable String id, JwtAuthenticationToken authentication) {
        logger.info("findById({}, {})", id, authentication);
        return new Foo();
    }

    @PostMapping("/foos")
    public Foo create(JwtAuthenticationToken authentication, @RequestBody CreateFooDTO createFooDTO) {
        String userId = authentication.getToken().getSubject();
        return fooService.createFoo(userId, createFooDTO);
    }
}
