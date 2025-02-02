package com.cloudrand.arcapi.service;

import com.cloudrand.arcapi.api.model.*;
import com.cloudrand.arcapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
