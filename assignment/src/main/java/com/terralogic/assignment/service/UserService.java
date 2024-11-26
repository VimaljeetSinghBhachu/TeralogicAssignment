package com.terralogic.assignment.service;

import com.terralogic.assignment.model.User;
import com.terralogic.assignment.utility.JwtUtil;
import com.terralogic.assignment.utility.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final Map<String, User> userStore = new HashMap<>();
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;

    public UserService(JwtUtil jwtUtil, PasswordUtil passwordUtil) {
        this.jwtUtil = jwtUtil;
        this.passwordUtil = passwordUtil;
    }

    public String addOrUpdateUser(User user) {
        logger.debug("Attempting to add or update user with username: {}", user.getUsername());

        // Append salt and hash the password
        String saltedPassword = passwordUtil.getSalt() + user.getPassword();
        user.setPassword(passwordUtil.hashPassword(saltedPassword));

        if (userStore.containsKey(user.getUsername())) {
            userStore.put(user.getUsername(), user);
            logger.info("Updated user with username: {}", user.getUsername());
            return "User updated successfully!";
        }
        userStore.put(user.getUsername(), user);
        logger.info("Added new user with username: {}", user.getUsername());
        logger.info("User pass: {}", user.getPassword());
        return "User created successfully.";
    }

    public List<User> getAllUsers() {
        logger.debug("Fetching all users.");
        return new ArrayList<>(userStore.values());
    }

    public String login(String username, String password) {
        logger.debug("Login attempt for username: {}", username);
        User user = userStore.get(username);
        if (user != null) {
            String saltedPassword = passwordUtil.getSalt() + password;
            if (passwordUtil.matchPassword(saltedPassword, user.getPassword())) {
                String token = jwtUtil.generateToken(user);
                logger.info("Login successful for username: {}", username);
                return token;
            }
        }
        logger.error("Invalid credentials for username: {}", username);
        return "Invalid credentials!";
    }

    public Map<String, String> validateTokenAndRetrieveDetails(String token) {
        Map<String, String> response = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            if (username == null || !jwtUtil.validateToken(token, username)) {
                response.put("error", "Invalid or expired token");
                return response;
            }
            User user = userStore.get(username);
            if (user == null) {
                response.put("error", "User not found");
                return response;
            }
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            return response;
        } catch (Exception e) {
            logger.error("An error occurred during token validation: {}", e.getMessage());
            response.put("error", "Internal server error");
            return response;
        }
    }

    public User getUserByUsername(String username) throws Exception {
        logger.debug("Get user if present");

        if (userStore.containsKey(username)) {
            logger.info("User fetched successfully.");
            return userStore.get(username);
        }
        throw new Exception("User not found!");
    }
}