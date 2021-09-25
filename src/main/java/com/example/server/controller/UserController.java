package com.example.server.controller;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserParam;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jingze Zheng
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public User addUser(@RequestBody UserParam user) {
        return userService.addUser(user);
    }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable String email) {
        return userService.getUser(email);
    }

    @PutMapping("/{email}")
    public User updateUser(@PathVariable String email, @RequestBody UserParam user) {
        return userService.updateUser(email, user);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}
