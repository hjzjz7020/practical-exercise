package com.example.server.controller;

import com.example.server.common.InternalException;
import com.example.server.entity.User;
import com.example.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * @author Jingze Zheng
 */
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getUser(@Valid @PathVariable("email") @Email String email) throws InternalException {
        User user = userService.getUser(email);
        if (user == null) {
            throw new InternalException("User not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Object> updateUser(@PathVariable("email") @Email String email, @Valid @RequestBody User user) throws InternalException {
        userService.updateUser(email, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Email String email) throws InternalException {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
