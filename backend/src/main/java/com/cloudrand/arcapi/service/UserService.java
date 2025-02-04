package com.cloudrand.arcapi.service;

import com.cloudrand.arcapi.api.model.AuthenticationResponse;
import com.cloudrand.arcapi.api.model.Role;
import com.cloudrand.arcapi.api.model.User;
import com.cloudrand.arcapi.jwt.JwtUtil;
import com.cloudrand.arcapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder  passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthenticationResponse register(User request){
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setProfilePicture(request.getProfilePicture());
        user.setRole(request.getRole());
        User savedUser = userRepository.save(user);
        System.out.println("User saved successfully: " + savedUser.getUsername());
        String token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }

    @Transactional
    public AuthenticationResponse authenticate(UserDetails request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token =jwtUtil.generateToken(user);
        return new AuthenticationResponse(token);
    }

    // Update user details
    @Transactional
    public ResponseEntity<?> updateUserDetails(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPhone() != null) {
                user.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getProfilePicture() != null) {
                user.setProfilePicture(updatedUser.getProfilePicture());
            }
            if (updatedUser.getUsername() != null) {
                if (userRepository.findByUsername(updatedUser.getUsername()).isEmpty()) {
                    user.setUsername(updatedUser.getUsername());
                }
                else {
                    return ResponseEntity.badRequest().body("Username not available");
                }
            }

            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    // Delete user
    public ResponseEntity<?> deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("User not found");
    }

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
