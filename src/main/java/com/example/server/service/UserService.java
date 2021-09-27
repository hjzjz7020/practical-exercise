package com.example.server.service;

import com.example.server.entity.dao.User;
import com.example.server.exception.InternalException;
import com.example.server.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

/** @author Jingze Zheng */
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User addUser(User user) throws InternalException {
    try {
      return userRepository.save(user);
    } catch (Exception ex) {
      throw new InternalException("Can not save this user.", HttpStatus.BAD_REQUEST);
    }
  }

  public User getUserByEmail(String email) throws InternalException {
    return userRepository
        .findById(email)
        .orElseThrow(() -> new InternalException("User not found", HttpStatus.NOT_FOUND));
  }

  public User updateUserByEmail(String email, User user) throws InternalException {
    User existingUser = this.getUserByEmail(email);

    if (!Objects.equals(email, user.getEmail())) {
      throw new InternalException(
          "Cannot update user email since email is unique ID", HttpStatus.BAD_REQUEST);
    }

    existingUser.setFirstName(user.getFirstName());
    existingUser.setLastName(user.getLastName());
    existingUser.setPassword(user.getPassword());
    try {
      return userRepository.save(existingUser);
    } catch (Exception ex) {
      throw new InternalException("Can not update this user.", HttpStatus.BAD_REQUEST);
    }
  }

  public void deleteUserByEmail(String email) throws InternalException {
    try {
      userRepository.deleteById(email);
    } catch (Exception ex) {
      throw new InternalException("Can not delete this user.", HttpStatus.BAD_REQUEST);
    }
  }
}
