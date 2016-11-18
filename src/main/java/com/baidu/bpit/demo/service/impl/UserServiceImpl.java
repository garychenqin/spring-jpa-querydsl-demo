package com.baidu.bpit.demo.service.impl;

import com.baidu.bpit.demo.dao.UserRepository;
import com.baidu.bpit.demo.model.QUser;
import com.baidu.bpit.demo.model.User;
import com.baidu.bpit.demo.service.UserService;
import com.google.common.base.Optional;
import com.mysema.query.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by chenshouqin on 2016-11-17 21:51.
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void insertUser(User user) {
        userRepository.save(user);
    }

    /**
     * 根据名字查询唯一用户
     *
     * @param name
     * @return
     */
    @Override
    public User findUniqueUserByName(String name) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QUser.user.name.eq(name));
        builder.andNot(QUser.user.age.gt(26));
        return userRepository.findOne(builder);
    }

    /**
     * 通过名字模糊查找所有用户
     *
     * @param name
     * @return
     */
    @Override
    public List<User> findBatchUserByName(String name) {
        BooleanBuilder builder = new BooleanBuilder();
        QUser qUser = QUser.user;
        builder.and(qUser.name.containsIgnoreCase(name));
        Page<User> usersPage = userRepository.findAll(builder, new PageRequest(0, 200));
        return usersPage.getContent();
    }

    /**
     * 通过Id查询唯一用户
     *
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        try {
            if (null == user.getId()) {
                throw new RuntimeException("user id cannot empty");
            }
            User oldUser = Optional.fromNullable(userRepository.findOne(user.getId())).orNull();
            if (null == oldUser) {
                throw new RuntimeException("user cannot found");
            }
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("update user failed", e);
        }
    }


}
