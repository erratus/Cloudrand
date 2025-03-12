package com.cloudrand.arcapi.service;

import com.cloudrand.arcapi.api.model.Role;
import com.cloudrand.arcapi.api.model.User;
import com.cloudrand.arcapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Create a new user with default role
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);  // Default role to USER
        return userRepository.save(user);
    }

    // Authenticate user
    public ResponseEntity<?> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    // Update user details
    public ResponseEntity<?> updateUserDetails(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.badRequest().body("User not found");
    }
    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }

    // Delete user
    public ResponseEntity<?> deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    // Password Reset & OTP (Mocked)
    public ResponseEntity<?> initiatePasswordReset(String email) {
        return ResponseEntity.ok("OTP sent to " + email);
    }

    public ResponseEntity<?> resetPassword(String email, String otp, String newPassword) {
        return ResponseEntity.ok("Password reset successful");
    }

    public ResponseEntity<?> resendOTP(String email) {
        return ResponseEntity.ok("OTP resent to " + email);
    }

    public ResponseEntity<?> verifyOTP(String email, String otp) {
        return ResponseEntity.ok("OTP verified successfully");
    }
}
