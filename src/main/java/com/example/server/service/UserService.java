package com.example.server.service;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserParam;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jingze Zheng
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(UserParam param) {
        User user = new User();
        user.setEmail(param.getEmail());
        user.setPassword(param.getPassword());
        user.setFirstName(param.getFirstName());
        user.setLastName(param.getLastName());

        return userRepository.save(user);
    }

    public User getUser(String email) {
        return userRepository.findById(email).orElse(null);
    }

    public User updateUser(UserParam param) {
        Optional<User> userFromBd = userRepository.findById(param.getEmail());

        if (userFromBd.isPresent()) {
            User userUpdate = userFromBd.get();
            userUpdate.setPassword(param.getPassword());
            userUpdate.setFirstName(param.getFirstName());
            userUpdate.setLastName(param.getLastName());

            return userRepository.save(userUpdate);
        } else {
            return null;
        }
    }

    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }
}
