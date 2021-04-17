package com.liverday.microservice.foo.service;

import com.liverday.microservice.foo.model.Foo;
import com.liverday.microservice.foo.model.dto.CreateFooDTO;
import com.liverday.microservice.foo.repositories.IFooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("unused")
public class FooService {

    @Autowired
    private IFooRepository fooRepository;

    public List<Foo> findAll(String userId) {
        return this.fooRepository.findAll(userId);
    }

    public Foo createFoo(String userId, CreateFooDTO createFooDTO) {
        Foo foo = this.fooRepository.create(userId, createFooDTO);
        this.fooRepository.save(foo);
        return foo;
    }
}
