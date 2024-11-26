package com.terralogic.assignment.utility;

import com.terralogic.assignment.constants.Constants;
import lombok.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordUtil {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final SecureRandom random = new SecureRandom();

    // Generate Salt from the configured saltKey value
    public String getSalt() {
        return Constants.SALT_KEY; // Now the salt is fetched from the configuration file
    }

    // Hash Password with Salt
    public String hashPassword(String saltedPassword) {
        return encoder.encode(saltedPassword);
    }

    // Match Password
    public boolean matchPassword(String saltedPassword, String hashedPassword) {
        return encoder.matches(saltedPassword, hashedPassword);
    }
}