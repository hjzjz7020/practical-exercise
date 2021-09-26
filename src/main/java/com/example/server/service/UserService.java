package com.example.server.service;

import com.example.server.common.InternalException;
import com.example.server.entity.User;
import com.example.server.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Jingze Zheng
 */
@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(String email) {
        return userRepository.findById(email).orElse(null);
    }

    public User updateUser(String email ,User user) throws InternalException {
        User existingUser = this.getUser(email);

        if (existingUser == null) {
            throw new InternalException("User not found", HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(email, user.getEmail())) {
            throw new InternalException("Cannot update user email since email is unique ID", HttpStatus.BAD_REQUEST);
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(String email) throws InternalException {
        try {
            userRepository.deleteById(email);
        }catch(Exception ex) {
            throw new InternalException("Can not delete this user.", HttpStatus.BAD_REQUEST);
        }
    }
}
