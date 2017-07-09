package com.baidu.bpit.demo.dao;

import com.baidu.bpit.demo.dao.extend.UserRepositoryNative;
import com.baidu.bpit.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by chenshouqin on 2016-11-17 21:26.
 */
public interface UserRepository extends QueryDslPredicateExecutor<User>, JpaRepository<User, Long>,
        UserRepositoryNative {
}
