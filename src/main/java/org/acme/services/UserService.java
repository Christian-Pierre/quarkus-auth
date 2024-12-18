package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.acme.models.User;
import org.acme.repositories.UserRepository;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public User authenticate(String username, String password) {
        return userRepository.find("username = ?1 and password = ?2", username, password).firstResult();
    }

     // Fetch all users
    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    // Fetch user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findByIdOptional(id);
    }

    // Create a new user
    @Transactional
    public User createUser(User user) {
        user.setToken(null); // Ensure token is null initially
        user.setTokenTime(null); // Ensure tokenTime is null initially
        userRepository.persist(user);
        return user;
    }

    // Update an existing user
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setToken(updatedUser.getToken());
        existingUser.setTokenTime(updatedUser.getTokenTime());
        return existingUser;
    }

    // Delete user by ID
    @Transactional
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.find("username", username).firstResultOptional();
    }

    // Update token and token time for a user
    @Transactional
    public void updateToken(Long id, String token) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        user.setToken(token);
        user.setTokenTime(Instant.now());
    }

    public boolean isTokenExpired(Long userId) {
        Optional<User> userOptional = userRepository.findByIdOptional(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        User user = userOptional.get();
        Instant tokenTime = user.getTokenTime();
        if (tokenTime == null) {
            return true; // Consider token expired if no tokenTime is set
        }
        Instant now = Instant.now();
        return Duration.between(tokenTime, now).getSeconds() > 3600;
    }
}