package com.mlesniak.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MessageDao {
    private Logger log = LoggerFactory.getLogger(MessageDao.class);

    private static MessageDao INSTANCE;
    private final EntityManagerFactory factory;

    private MessageDao() {
        factory = Persistence.createEntityManagerFactory("database");
    }

    public static MessageDao get() {
        if (INSTANCE == null) {
            INSTANCE = new MessageDao();
        }

        return INSTANCE;
    }

    public void write(Message message) {
        log.debug("Persisting: " + message);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(message);
        em.getTransaction().commit();
        em.close();
    }
}
