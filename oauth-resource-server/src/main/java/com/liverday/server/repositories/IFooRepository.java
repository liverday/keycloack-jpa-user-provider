package com.liverday.server.repositories;

import com.liverday.server.model.Foo;
import com.liverday.server.model.dto.CreateFooDTO;

import java.util.List;
import java.util.Optional;

public interface IFooRepository {
    Optional<Foo> findById(String userId, String id);
    List<Foo> findAll(String userId);
    Foo create(CreateFooDTO createFooDTO);
    Foo save(Foo foo);
    Foo update(Foo foo) throws Exception;
    boolean delete(Foo foo);
}
