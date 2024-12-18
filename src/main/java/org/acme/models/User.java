package org.acme.models;

import java.time.Duration;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    @Column(length = 1000, nullable = true)
    private String token;
    private Instant tokenTime;    

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public Instant getTokenTime() { return tokenTime; }
    public void setTokenTime(Instant tokenTime) { this.tokenTime = tokenTime; }

    // Method to check if the token has expired
    public boolean isTokenExpired() {
        if (tokenTime == null) {
            return true; // Consider token expired if no tokenTime is set
        }
        Instant now = Instant.now();
        return Duration.between(tokenTime, now).getSeconds() > 3600;
    }
}
