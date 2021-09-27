package com.example.server.controller;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserDto;
import com.example.server.exception.InternalException;
import com.example.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

/** @author Jingze Zheng */
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  ModelMapper modelMapper = new ModelMapper();

  @PostMapping()
  public ResponseEntity<Void> addUser(@Valid @RequestBody UserDto userDto)
      throws InternalException {
    userService.addUser(modelMapper.map(userDto, User.class));
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{email}")
  public ResponseEntity<User> getUserByEmail(@Valid @PathVariable("email") @Email String email)
      throws InternalException {
    User user = userService.getUserByEmail(email);
    return ResponseEntity.ok().body(user);
  }

  @PutMapping("/{email}")
  public ResponseEntity<Void> updateUserByEmail(
      @PathVariable("email") @Email String email, @Valid @RequestBody UserDto userDto)
      throws InternalException {
    userService.updateUserByEmail(email, modelMapper.map(userDto, User.class));
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{email}")
  public ResponseEntity<Void> deleteUserByEmail(@PathVariable @Email String email)
      throws InternalException {
    userService.deleteUserByEmail(email);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
