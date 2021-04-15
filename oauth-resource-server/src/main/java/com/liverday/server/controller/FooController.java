package com.liverday.server.controller;

import com.liverday.server.model.Foo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foos")
public class FooController {

    @GetMapping("/{id}")
    public Foo findById(@PathVariable String id) {
        return new Foo();
    }
}
