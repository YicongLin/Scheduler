package com.scheduler.scheduler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.scheduler.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

}
