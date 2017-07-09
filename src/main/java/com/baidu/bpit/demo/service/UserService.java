package com.baidu.bpit.demo.service;

import com.baidu.bpit.demo.model.User;

import java.util.List;

/**
 * Created by chenshouqin on 2016-11-17 21:25.
 */
public interface UserService {

    void insertUser(User user);
    User findUniqueUserByName(String name);
    List<User> findBatchUserByName(String name);
    User findById(Long id);
    void updateUser(User user);
    void deleteUser();
}
