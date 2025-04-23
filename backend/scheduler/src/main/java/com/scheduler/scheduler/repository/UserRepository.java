package com.scheduler.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.scheduler.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
