package com.terralogic.assignment.controller;

import com.terralogic.assignment.model.User;
import com.terralogic.assignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        logger.debug("Received request to create or update user: {}", user.getUsername());
        String response = userService.addOrUpdateUser(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.debug("Received request to fetch all users.");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping
    public ResponseEntity<User> getAllUsers(@RequestParam String username) throws Exception {
        logger.debug("Received request to fetch all users.");
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User loginRequest) {
        logger.debug("Received login request for username: {}", loginRequest.getUsername());
        String token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (token.equals("Invalid credentials!")) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password!"));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestParam String token) {
        logger.debug("Received request to validate token");
        Map<String, String> response = userService.validateTokenAndRetrieveDetails(token);
        if (response.containsKey("error")) {
            String error = response.get("error");
            return ResponseEntity.status(error.equals("User not found") ? 404 : 401).body(response);
        }
        return ResponseEntity.ok(response);
    }
}