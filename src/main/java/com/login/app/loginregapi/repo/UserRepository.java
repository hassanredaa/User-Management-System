package com.login.app.loginregapi.repo;

import com.login.app.loginregapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByUsername(String username);
}
