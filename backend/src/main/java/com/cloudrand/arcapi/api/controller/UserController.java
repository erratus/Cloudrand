package com.cloudrand.arcapi.api.controller;

import com.cloudrand.arcapi.api.model.User;
import com.cloudrand.arcapi.api.model.Role;
import com.cloudrand.arcapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, @RequestParam String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); // Default role
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        return userService.authenticateUser(email, password);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok("User logged out successfully.");
    }

    @PutMapping("/updatedetails")
    public ResponseEntity<?> updateUser(@RequestParam Long userId, @RequestBody User updatedUser) {
        return userService.updateUserDetails(userId, updatedUser);
    }

    @DeleteMapping("/deleteme")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        return userService.deleteUser(userId);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return userService.initiatePasswordReset(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        return userService.resetPassword(email, otp, newPassword);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOTP(@RequestParam String email) {
        return userService.resendOTP(email);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return userService.verifyOTP(email, otp);
    }
}
