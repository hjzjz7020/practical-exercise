package com.example.server.repository;

import com.example.server.entity.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jingze Zheng
 */
public interface UserRepository extends JpaRepository<User, String> {
}
