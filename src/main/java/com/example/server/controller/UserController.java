package com.example.server.controller;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserDto;
import com.example.server.exception.InternalException;
import com.example.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

/** @author Jingze Zheng */
@Api(value = "A Spring boot RESTful API practical exercise.")
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  ModelMapper modelMapper = new ModelMapper();

  @ApiOperation(value = "Creates a new user.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "The user was created successfully."),
        @ApiResponse(code = 400, message = "Can not creat this user.")
      })
  @PostMapping()
  public ResponseEntity<Void> addUser(@Valid @RequestBody UserDto userDto)
      throws InternalException {
    userService.addUser(modelMapper.map(userDto, User.class));
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @ApiOperation(value = "Retrieve an existing user.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Get a user by email."),
        @ApiResponse(code = 400, message = "Can not get this user.."),
        @ApiResponse(code = 404, message = "User not found.")
      })
  @GetMapping("/{email}")
  public ResponseEntity<User> getUserByEmail(@Valid @PathVariable("email") @Email String email)
      throws InternalException {
    User user = userService.getUserByEmail(email);
    return ResponseEntity.ok().body(user);
  }

  @ApiOperation(value = "Update an existing user.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "The user was updated successfully."),
        @ApiResponse(code = 400, message = "Can not update this user.."),
        @ApiResponse(code = 404, message = "User not found.")
      })
  @PutMapping("/{email}")
  public ResponseEntity<Void> updateUserByEmail(
      @PathVariable("email") @Email String email, @Valid @RequestBody UserDto userDto)
      throws InternalException {
    userService.updateUserByEmail(email, modelMapper.map(userDto, User.class));
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ApiOperation(value = "Delete an existing user.")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "The user was deleted successfully."),
        @ApiResponse(code = 400, message = "Can not delete this user."),
      })
  @DeleteMapping("/{email}")
  public ResponseEntity<Void> deleteUserByEmail(@PathVariable @Email String email)
      throws InternalException {
    userService.deleteUserByEmail(email);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
