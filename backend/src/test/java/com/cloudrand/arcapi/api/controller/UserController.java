package com.cloudrand.arcapi.api.controller;

import com.cloudrand.arcapi.api.model.AuthenticationResponse;
import com.cloudrand.arcapi.api.model.User;
import com.cloudrand.arcapi.api.model.Role;
import com.cloudrand.arcapi.jwt.JwtUtil;
import com.cloudrand.arcapi.service.UserService;
import com.cloudrand.arcapi.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody User user , @RequestParam String confirmPassword) {

        if (!user.getPassword().equals(confirmPassword)) {
            AuthenticationResponse authenticationResponse =new AuthenticationResponse("Passwords do not match!");
            return ResponseEntity.badRequest().body(authenticationResponse);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); // Default role
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/demo")
    public ResponseEntity<String> demo(  ) {
        return ResponseEntity.ok("hello from demo");
    }

    //may change
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUserByUsername( @RequestParam String loginCredentials,@RequestParam String password ) {
        try{
            final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = EMAIL_PATTERN.matcher(loginCredentials);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginCredentials, password));
            UserDetails userDetails =matcher.matches()? userDetailsService.loadUserByEmail(loginCredentials):userDetailsService.loadUserByUsername(loginCredentials);
            return  ResponseEntity.ok(userService.authenticate(userDetails));
        }
        catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken",e);
            AuthenticationResponse authenticationResponse =new AuthenticationResponse("Incorrect ");
            return ResponseEntity.badRequest().body(authenticationResponse);
        }

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

