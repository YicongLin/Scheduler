package com.scheduler.scheduler.entity;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Profile() {
        this.bio = ""; // Empty bio by default
    }

    public Profile(User user) {
        this();
        this.user = user;
        this.userName = user.getEmail(); // Default username is email
        this.avatarUrl = "temp";
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User getUser() {
        return user;
    }


}
