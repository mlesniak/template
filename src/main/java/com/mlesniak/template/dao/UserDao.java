package com.mlesniak.template.dao;

import com.mlesniak.template.config.Config;
import com.mlesniak.template.config.ConfigKeys;
import com.mlesniak.template.model.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserDao extends BaseDao {
    private Logger log = LoggerFactory.getLogger(UserDao.class);

    private static UserDao INSTANCE;

    public static UserDao get() {
        if (INSTANCE == null) {
            INSTANCE = new UserDao();
        }

        return INSTANCE;
    }

    public void addAdminUser() {
        Config config = Config.get();
        if (getByUsername(config.get(ConfigKeys.ADMIN_USERNAME)) != null) {
            return;
        }

        UserDO admin = new UserDO();
        admin.setUsername(config.get(ConfigKeys.ADMIN_USERNAME));
        admin.setPassword(config.get(ConfigKeys.ADMIN_PASSWORD));
        admin.setEmail(config.get(ConfigKeys.ADMIN_EMAIL));
        admin.setRoles(config.get(ConfigKeys.ADMIN_ROLES));
        add(admin);
    }

    public UserDO getByUsername(String username) {
        EntityManager em = getEntityManager();
        TypedQuery<UserDO> query = em.createQuery("SELECT u FROM UserDO u where u.username = :username", UserDO.class);
        query.setParameter("username", username);
        List<UserDO> resultList = query.getResultList();
        em.close();

        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    public void add(UserDO user) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }
}
