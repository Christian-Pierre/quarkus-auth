package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.acme.models.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public User authenticate(String username, String password) {
        return userRepository.find("username = ?1 and password = ?2", username, password).firstResult();
    }
}

@ApplicationScoped
class UserRepository implements PanacheRepository<User> {
    // Custom queries if necessary
}