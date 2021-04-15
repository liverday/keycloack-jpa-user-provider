package com.liverday.server.repositories.implementations;

import com.liverday.server.model.Foo;
import com.liverday.server.model.dto.CreateFooDTO;
import com.liverday.server.repositories.IFooRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryFooRepository implements IFooRepository {
    private List<Foo> foos = new ArrayList<>();

    @Override
    public Optional<Foo> findById(String userId, String id) {
        return foos
                .stream()
                .filter(foo -> foo.getUserId().equals(userId) && foo.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Foo> findAll(String userId) {
        return foos
                .stream()
                .filter(foo -> foo.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Foo create(CreateFooDTO createFooDTO) {
        Foo foo = new Foo();

        foo.setId(UUID.randomUUID().toString());
        foo.setUserId(createFooDTO.getUserId());
        foo.setName(createFooDTO.getName());
        foo.setBar(createFooDTO.getBar());

        return foo;
    }

    @Override
    public Foo save(Foo foo) {
        foos.add(foo);
        return foo;
    }

    @Override
    public Foo update(Foo foo) throws Exception {
        return foos
                .stream()
                .filter(savedFoo -> foo.getId().equals(savedFoo.getId()))
                .peek(fooToUpdate -> {
                    fooToUpdate.setName(foo.getName());
                    fooToUpdate.setBar(foo.getBar());
                })
                .findFirst()
                .orElseThrow(() -> new Exception("FOO NOT FOUND"));
    }

    @Override
    public boolean delete(Foo foo) {
        this.foos = foos
                .stream()
                .filter(savedFoo -> !savedFoo.getId().equals(foo.getId()))
                .collect(Collectors.toList());

        return true;
    }
}
