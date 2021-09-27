package com.example.server.unit.controllerTest;

import com.example.server.controller.UserController;
import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserDto;
import com.example.server.exception.InternalException;
import com.example.server.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {

  private UserService userService;

  private UserController userController;

  ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  void initUseCase() {
    userService = mock(UserService.class);
    userController = new UserController(userService);
  }

  @Test
  void testAddUser() throws InternalException {
    UserDto userDto = new UserDto();
    userDto.setEmail("zjz@gmail.com");
    userDto.setPassword("xxxxx");
    userDto.setFirstName("A");
    userDto.setLastName("B");

    User user = modelMapper.map(userDto, User.class);

    when(userService.addUser(user)).thenReturn(user);

    ResponseEntity<Void> response = userController.addUser(userDto);

    Assertions.assertEquals(response, new ResponseEntity<>(HttpStatus.CREATED));

    verify(userService).addUser(refEq(user));
  }

  @Test
  void testGetUser() throws InternalException {
    String email = "zjz@gmail.com";
    User user = new User(email, "xxxxx", "A", "B");

    when(userService.getUserByEmail(email)).thenReturn(user);

    ResponseEntity<User> response = userController.getUserByEmail(email);

    Assertions.assertEquals(response, new ResponseEntity<>(user, HttpStatus.OK));

    verify(userService).getUserByEmail(email);
  }

  @Test
  void testGetUserThrowIfUserNotFound() throws InternalException {
    String email = "zjz@gmail.com";

    doThrow(new InternalException("User not found", HttpStatus.NOT_FOUND))
        .when(userService)
        .getUserByEmail(email);

    InternalException exception =
        assertThrows(InternalException.class, () -> userController.getUserByEmail(email));

    Assertions.assertSame("User not found", exception.getMessage());
    Assertions.assertSame(HttpStatus.NOT_FOUND, exception.getHttpStatus());

    verify(userService).getUserByEmail(email);
  }

  @Test
  void testUpdateUser() throws InternalException {
    String email = "zjz@gmail.com";

    UserDto userDto = new UserDto();
    userDto.setEmail(email);
    userDto.setPassword("xxxxx");
    userDto.setFirstName("A");
    userDto.setLastName("B");

    User user = modelMapper.map(userDto, User.class);

    when(userService.updateUserByEmail(email, user)).thenReturn(user);

    ResponseEntity<Void> response = userController.updateUserByEmail(email, userDto);

    Assertions.assertEquals(response, new ResponseEntity<>(HttpStatus.NO_CONTENT));

    verify(userService).updateUserByEmail(refEq(email), refEq(user));
  }

  @Test
  void testUpdateUserBubbleExceptionFromService() throws InternalException {
    String email = "zjz@gmail.com";

    UserDto userDto = new UserDto();
    userDto.setEmail(email);
    userDto.setPassword("xxxxx");
    userDto.setFirstName("A");
    userDto.setLastName("B");

    User user = modelMapper.map(userDto, User.class);

    Exception ex =
        new InternalException(
            "Cannot update user email since email is unique ID", HttpStatus.BAD_REQUEST);

    doThrow(ex).when(userService).updateUserByEmail(any(), any());

    InternalException exception =
        assertThrows(
            InternalException.class, () -> userController.updateUserByEmail(email, userDto));

    Assertions.assertSame(exception, ex);

    verify(userService).updateUserByEmail(refEq(email), refEq(user));
  }

  @Test
  void testDeleteUser() throws InternalException {
    String email = "zjz@gmail.com";

    doNothing().when(userService).deleteUserByEmail(email);

    ResponseEntity<Void> response = userController.deleteUserByEmail(email);

    Assertions.assertEquals(response, new ResponseEntity<>(HttpStatus.NO_CONTENT));

    verify(userService).deleteUserByEmail(email);
  }

  @Test
  void testDeleteUserBubbleExceptionFromService() throws InternalException {
    String email = "zjz@gmail.com";

    Exception ex = new InternalException("User not found", HttpStatus.NOT_FOUND);

    doThrow(ex).when(userService).deleteUserByEmail(email);

    InternalException exception =
        assertThrows(InternalException.class, () -> userController.deleteUserByEmail(email));

    Assertions.assertSame(exception, ex);
  }
}
