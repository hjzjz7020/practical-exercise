package com.example.server.controller;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.Error;
import com.example.server.entity.pojo.UserParam;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> addUser(@RequestBody UserParam user) {
        return ResponseEntity.created().body(userService.addUser(user));
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.getUser(email));
    }

    @PutMapping("/{email}")
    public ResponseEntity<Object> updateUser(@RequestBody UserParam user) {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Error> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}
