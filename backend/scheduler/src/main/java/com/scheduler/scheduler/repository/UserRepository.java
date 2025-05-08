package com.scheduler.scheduler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.scheduler.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
