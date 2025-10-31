package com.rps.goldloan.service;

import com.rps.goldloan.dto.UserRequest;
import com.rps.goldloan.dto.UserResponse;
import com.rps.goldloan.dto.UserUpdateDto;
import com.rps.goldloan.entity.User;
import com.rps.goldloan.exception.UserCreationException;
import com.rps.goldloan.exception.UserNotFoundException;
import com.rps.goldloan.exception.UserUpdateException;
import com.rps.goldloan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BranchService branchService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    public UserResponse createUser(UserRequest userRequest) {
        try {
            validateUserRequest(userRequest);

            User user = new User();
            user.setUsername(userRequest.getUsername());
            user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
            user.setFullName(userRequest.getFullName());
            user.setEmail(userRequest.getEmail());
            user.setRole(userRequest.getRole());
            

            user.setBranch(branchService.getBranch(1L));  //we are setting default branch it can be chnged later when admin created more branches

            
            user.setActive(userRequest.isActive());
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            user = userRepository.save(user);
            emailService.sendUserCreationEmail(user);
            return userToUserResponse(user);
        } catch (IllegalArgumentException e) {
            throw new UserCreationException(e.getMessage());
        } catch (Exception e) {
            throw new UserCreationException("Error creating user: " + e.getMessage());
        }
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return userToUserResponse(user);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(userToUserResponse(user));
        }
        return userResponses;
    }

    public UserResponse updateUser(Long userId, UserUpdateDto userUpdateDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

            if (Objects.nonNull(userUpdateDto.getUsername()) && !userUpdateDto.getUsername().isEmpty()) {
                user.setUsername(userUpdateDto.getUsername());
            }

            if (Objects.nonNull(userUpdateDto.getPassword()) && !userUpdateDto.getPassword().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(userUpdateDto.getPassword()));
            }

            if (Objects.nonNull(userUpdateDto.getFullName()) && !userUpdateDto.getFullName().isEmpty()) {
                user.setFullName(userUpdateDto.getFullName());
            }

            if (Objects.nonNull(userUpdateDto.getEmail()) && !userUpdateDto.getEmail().isEmpty()) {
                user.setEmail(userUpdateDto.getEmail());
            }

            if (Objects.nonNull(userUpdateDto.getRole())) {
                user.setRole(userUpdateDto.getRole());
            }

            if (Objects.nonNull(userUpdateDto.getBranchId())) {
                user.setBranch(branchService.getBranch(userUpdateDto.getBranchId()));
            }

            if (Objects.nonNull(userUpdateDto.getActive())) {
                user.setActive(userUpdateDto.getActive());
            }

            user.setUpdatedAt(LocalDateTime.now());
            user = userRepository.save(user);

            return userToUserResponse(user);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserUpdateException("Error updating user with ID: " + userId + ". " + e.getMessage());
        }
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private void validateUserRequest(UserRequest userRequest) {
        if (Objects.isNull(userRequest)) {
            throw new IllegalArgumentException("User request cannot be null");
        }
        if (Objects.isNull(userRequest.getUsername()) || userRequest.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required and cannot be empty");
        }
        if (Objects.isNull(userRequest.getPassword()) || userRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required and cannot be empty");
        }
        if (Objects.isNull(userRequest.getFullName()) || userRequest.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required and cannot be empty");
        }
        if (Objects.isNull(userRequest.getEmail()) || userRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required and cannot be empty");
        }
        if (Objects.isNull(userRequest.getRole())) {
            throw new IllegalArgumentException("Role is required and cannot be null");
        }
    }

    private UserResponse userToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        if (Objects.nonNull(user.getBranch())) {
            userResponse.setBranchId(user.getBranch().getId());
        }
        userResponse.setActive(user.isActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }
}
