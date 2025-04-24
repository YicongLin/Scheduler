package com.scheduler.scheduler.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254, updatable = false)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @OneToOne(mappedBy = "user", 
              cascade = CascadeType.ALL, 
              orphanRemoval = true, 
              fetch = FetchType.LAZY)
    private Profile profile;

    public User() {
        this.createdAt = LocalDate.now();
    }

    public User(String email, String password) {
        this();
        this.email = email;
        this.password = password;
        this.profile = new Profile(this);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
