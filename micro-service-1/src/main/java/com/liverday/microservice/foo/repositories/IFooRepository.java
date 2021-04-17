package com.liverday.microservice.foo.repositories;

import com.liverday.microservice.foo.model.Foo;
import com.liverday.microservice.foo.model.dto.CreateFooDTO;

import java.util.List;
import java.util.Optional;


public interface IFooRepository {
    Optional<Foo> findById(String userId, String id);
    List<Foo> findAll(String userId);
    Foo create(String userId, CreateFooDTO createFooDTO);
    Foo save(Foo foo);
    Foo update(Foo foo) throws Exception;
    boolean delete(Foo foo);
}
