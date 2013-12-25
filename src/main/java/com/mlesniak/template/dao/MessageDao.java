package com.mlesniak.template.dao;

import com.mlesniak.template.model.MessageDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class MessageDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(MessageDao.class);

    private static MessageDao INSTANCE;

    public static MessageDao get() {
        if (INSTANCE == null) {
            INSTANCE = new MessageDao();
        }

        return INSTANCE;
    }

    public void write(MessageDO message) {
        log.debug("Persisting: " + message);
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(message);
        em.getTransaction().commit();
        em.close();
    }
}
