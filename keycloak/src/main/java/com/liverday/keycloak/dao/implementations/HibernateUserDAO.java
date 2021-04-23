package com.liverday.keycloak.dao.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.liverday.keycloak.dao.IUserDAO;
import com.liverday.keycloak.model.User;

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
    public List<User> findAll(Integer firstResult, Integer maxResults) {
        logger.info("findAll({}, {})", firstResult, maxResults);
        TypedQuery<User> query = em.createNamedQuery("getUsers", User.class);
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        query.setParameter("search", "%");
        return query.getResultList();
    }

    @Override
    public Long count() {
        return em.createNamedQuery("getUsersCount", Long.class).getSingleResult();
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
    public List<User> searchUserByUserNameOrEmail(String search) {
        logger.info("searchUserByUserNameOrEmail({})", search);
        return searchUserByUserNameOrEmail(search, null, null);
    }

    @Override
    public List<User> searchUserByUserNameOrEmail(String search, Integer firstResult, Integer maxResults) {
        logger.info("searchUserByUserNameOrEmail({}, {}, {})", search, firstResult, maxResults);
        TypedQuery<User> query = em.createNamedQuery("getUsers", User.class);
        query.setParameter("search", "%" + search + "%");
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    @Override
    public User create(User user) {
        logger.info("HibernateUserDAO#create({})", user);
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
        logger.info("HibernateUserDAO#remove({})", user);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.remove(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public User update(User user) {
        logger.info("HibernateUserDAO#update({})", user);
        EntityTransaction transaction = em.getTransaction();
        User updatedUser = null;
        transaction.begin();

        try {
            updatedUser = em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        logger.info("HibernateUserDAO#update({}) result: {}", user, updatedUser);

        return updatedUser;
    }

    @Override
    public void close() {
        em.close();
    }
}
