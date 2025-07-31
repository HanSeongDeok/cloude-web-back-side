package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {
    final private UserRepository userRepository;

    public boolean hasUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Optional<UserEntity> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}