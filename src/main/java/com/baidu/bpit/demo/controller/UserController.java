package com.baidu.bpit.demo.controller;

import com.baidu.bpit.demo.model.User;
import com.baidu.bpit.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by chenshouqin on 2016-11-17 21:58.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/addUser")
    public void addUser(User user) {
        userService.insertUser(user);
    }

    @ResponseBody
    @RequestMapping("/findUniqueUser")
    public User findUniqueUserByName(@RequestParam("name") String username) {
        return userService.findUniqueUserByName(username);
    }

    @ResponseBody
    @RequestMapping("/findBatch")
    public List<User> findBatch(@RequestParam("name") String username) {
        return userService.findBatchUserByName(username);
    }

    @RequestMapping("/updateUser")
    public void updateInfo(User user) {
        userService.updateUser(user);
    }
}
