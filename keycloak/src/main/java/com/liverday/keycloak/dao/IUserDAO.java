package com.liverday.keycloak.dao;

import com.liverday.keycloak.model.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    EntityManager getEntityManager();

    List<User> findAll();
    List<User> findAll(Integer firstResult, Integer maxResults);
    Long count();

    Optional<User> findUserById(String id);
    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByEmail(String email);

    List<User> searchUserByUserNameOrEmail(String search);
    List<User> searchUserByUserNameOrEmail(String search, Integer firstResult, Integer maxResults);

    User create(User user);
    void remove(User user);
    User update(User user);

    void close();
}
