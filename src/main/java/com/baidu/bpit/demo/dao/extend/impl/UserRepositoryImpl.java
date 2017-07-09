package com.baidu.bpit.demo.dao.extend.impl;

import com.baidu.bpit.demo.dao.extend.UserRepositoryNative;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by chenshouqin on 2017/7/9
 */
public class UserRepositoryImpl implements UserRepositoryNative {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public void deleteAllP() {
        em.createQuery("DELETE FROM User u where u.id = 1").executeUpdate();
    }
}
