package com.example.server.unit.serviceTest;

import com.example.server.entity.dao.User;
import com.example.server.exception.InternalException;
import com.example.server.repository.UserRepository;
import com.example.server.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void initUseCase() {
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);
  }

  @Test
  void testAddUser() throws InternalException {
    User user = new User("zjz@gmail.com", "xxxxx", "A", "B");

    when(userRepository.save(user)).thenReturn(user);

    User newUser = userService.addUser(user);

    Assertions.assertEquals(user, newUser);

    verify(userRepository).save(user);
  }

  @Test
  void testGetUser() throws InternalException {
    String email = "zjz@gmail.com";

    User existingUser = new User(email, "xxxxx", "A", "B");
    Optional<User> optionalExistingUser = Optional.of(existingUser);

    when(userRepository.findById(email)).thenReturn(optionalExistingUser);

    User user = userService.getUserByEmail(email);

    Assertions.assertEquals(user, existingUser);
    verify(userRepository).findById(email);
  }

  @Test
  void testReturnNullWhenCalledGetUser() {
    String email = "zjz@gmail.com";

    Optional<User> optionalExistingUser = Optional.empty();

    when(userRepository.findById(email)).thenReturn(optionalExistingUser);

    InternalException exception =
        assertThrows(InternalException.class, () -> userService.getUserByEmail(email));

    Assertions.assertSame("User not found", exception.getMessage());
    Assertions.assertSame(HttpStatus.NOT_FOUND, exception.getHttpStatus());

    verify(userRepository).findById(email);
  }

  @Test
  void testUpdateUser() throws InternalException {

    String email = "zjz@gmail.com";
    String password = "xxxxx";
    String firstName = "A";
    String lastName = "B";

    User existingUser = new User(email, password, firstName, lastName);
    Optional<User> optionalExistingUser = Optional.of(existingUser);

    when(userRepository.findById(email)).thenReturn(optionalExistingUser);

    User newUser = new User(email, password, "ff", lastName);

    when(userRepository.save(any())).thenReturn(newUser);
    User updatedUser = userService.updateUserByEmail(email, existingUser);

    Assertions.assertEquals(newUser, updatedUser);

    verify(userRepository).findById(email);
    verify(userRepository).save(any());
  }

  @Test
  void testUpdateUserThrowWhenUserNotFound() {
    String email = "zjz@gmail.com";
    User user = new User();
    Optional<User> optionalExistingUser = Optional.empty();

    when(userRepository.findById(email)).thenReturn(optionalExistingUser);

    InternalException exception =
        assertThrows(InternalException.class, () -> userService.updateUserByEmail(email, user));

    Assertions.assertSame("User not found", exception.getMessage());
    Assertions.assertSame(HttpStatus.NOT_FOUND, exception.getHttpStatus());

    verify(userRepository).findById(email);
    verify(userRepository, never()).save(any());
  }

  @Test
  void testUpdateUserThrowWhenEmailIdentical() {
    String email = "zjz@gmail.com";
    String password = "xxxxx";
    String firstName = "A";
    String lastName = "B";

    User existingUser = new User(email, password, firstName, lastName);
    Optional<User> optionalExistingUser = Optional.of(existingUser);

    when(userRepository.findById(any())).thenReturn(optionalExistingUser);

    InternalException exception =
        assertThrows(
            InternalException.class,
            () -> userService.updateUserByEmail("abc@gmail.com", existingUser));

    Assertions.assertSame(
        "Cannot update user email since email is unique ID", exception.getMessage());
    Assertions.assertSame(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    verify(userRepository).findById("abc@gmail.com");
    verify(userRepository, never()).save(any());
  }

  @Test
  void testDeleteUser() throws InternalException {
    String email = "zjz@gamil.com";

    doNothing().when(userRepository).deleteById(email);

    userService.deleteUserByEmail(email);

    verify(userRepository).deleteById(email);
  }

  @Test
  void testThrowWhenDeletingUserFailed() {
    String email = "zjz@gamil.com";

    doThrow(new RuntimeException("error")).when(userRepository).deleteById(email);

    InternalException exception =
        assertThrows(InternalException.class, () -> userService.deleteUserByEmail(email));

    Assertions.assertSame("Can not delete this user.", exception.getMessage());
    Assertions.assertSame(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    verify(userRepository).deleteById(email);
  }
}
