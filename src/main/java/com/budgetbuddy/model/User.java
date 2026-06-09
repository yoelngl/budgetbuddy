package com.budgetbuddy.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username tidak boleh kosong")
    @Column(unique = true, nullable = false)
    private String username;

    @Email(message = "Format email tidak valid")
    @NotBlank(message = "Email tidak boleh kosong")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
    }

    public Long          getId()                       { return id; }
    public void          setId(Long id)                { this.id = id; }

    public String        getUsername()                  { return username; }
    public void          setUsername(String username)   { this.username = username; }

    public String        getEmail()                    { return email; }
    public void          setEmail(String email)        { this.email = email; }

    @JsonIgnore
    public String        getPassword()                 { return password; }
    public void          setPassword(String password)  { this.password = password; }

    public LocalDateTime getCreatedAt()                { return createdAt; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username=" + username + ", email=" + email + "}";
    }
}
