package com.example.server.service;

import com.example.server.entity.dao.User;
import com.example.server.entity.pojo.UserParam;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User updateUser(String email, UserParam param) {
        User userFromDb = userRepository.findById(email).orElse(null);
        userFromDb.setEmail(param.getEmail());
        userFromDb.setPassword(param.getPassword());
        userFromDb.setFirstName(param.getFirstName());
        userFromDb.setLastName(param.getLastName());

        return userRepository.save(userFromDb);
    }

    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }
}
