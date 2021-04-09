package valemobi.auth.dao;

import valemobi.auth.model.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    EntityManager getEntityManager();

    List<User> findAll();
    List<User> findAll(Integer page, Integer size);
    List<User> count();

    Optional<User> findUserById(String id);
    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByEmail(String email);

    User create(User user);
    void remove(User user);
    User update(User user);

    void close();
}
