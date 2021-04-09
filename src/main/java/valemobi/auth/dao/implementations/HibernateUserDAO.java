package valemobi.auth.dao.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valemobi.auth.dao.IUserDAO;
import valemobi.auth.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HibernateUserDAO implements IUserDAO {
    private final EntityManager em;
    Logger logger = LoggerFactory.getLogger(HibernateUserDAO.class);

    public HibernateUserDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<User> findAll() {
        return findAll(null, null);
    }

    @Override
    public List<User> findAll(Integer page, Integer size) {
        logger.info("findAll({}, {})", page, size);
        TypedQuery<User> query = em.createNamedQuery("getUsers", User.class);
        if (page != null && size != null) {
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    @Override
    public List<User> count() {
        TypedQuery<User> query = em.createNamedQuery("getUsers", User.class);
        return query.getResultList();
    }

    @Override
    public Optional<User> findUserById(String id) {
        logger.info("findUserById({})", id);
        User user = em.find(User.class, UUID.fromString(id));
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> findUserByUserName(String userName) {
        logger.info("findUserByUserName({})", userName);
        TypedQuery<User> query = em.createNamedQuery("getUserByUserName", User.class);
        query.setParameter("userName", userName);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        logger.info("findUserByEmail({})", email);
        TypedQuery<User> query = em.createNamedQuery("getUserByEmail", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public User create(User user) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        return user;
    }

    @Override
    public void remove(User user) {
        EntityTransaction transaction = em.getTransaction();

        try {
            em.remove(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public User update(User user) {
        EntityTransaction transaction = em.getTransaction();
        User updatedUser = null;

        try {
            updatedUser = em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        return updatedUser;
    }

    @Override
    public void close() {
        em.close();
    }
}
