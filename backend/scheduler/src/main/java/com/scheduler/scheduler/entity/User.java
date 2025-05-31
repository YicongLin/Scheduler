package com.scheduler.scheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "profile")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254, updatable = false)
    private String email;

    @Column(nullable = false, unique = true, length = 254)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @OneToOne(mappedBy = "user", 
              cascade = CascadeType.ALL, 
              orphanRemoval = true, 
              fetch = FetchType.LAZY)
    private Profile profile;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }

        if (profile != null && profile.getUser() == null) {
            profile.setUser(this);
        }
    }
}
