// UserService.java
package com.reviewplatform.service;

import com.reviewplatform.dto.UserRegistrationDto;
import com.reviewplatform.dto.UserUpdateDto;
import com.reviewplatform.exception.ResourceAlreadyExistsException;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.User;
import com.reviewplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists: " + registrationDto.getUsername());
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + registrationDto.getEmail());
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setRole(User.UserRole.USER);
        user.setActive(true);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, UserUpdateDto updateDto) {
        User user = getUserById(userId);

        if (updateDto.getBio() != null) {
            user.setBio(updateDto.getBio());
        }

        if (updateDto.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateDto.getProfileImageUrl());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}