package com.example.server.controller;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserParam;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jingze Zheng
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @GetMapping("/{email}")
//    public User getByEmail() {
//        return userService.getByEmail();
//    }

    @PostMapping
    public User addUser(@RequestBody UserParam user) {
        return userService.addUser(user);
    }
}
