package com.liverday.server.service;

import com.liverday.server.repositories.IFooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FooService {

    @Autowired
    private IFooRepository fooRepository;


}
