package com.rps.goldloan.controller;

import com.rps.goldloan.dto.LoginRequest;
import com.rps.goldloan.dto.LoginResponse;
import com.rps.goldloan.dto.UserRequest;
import com.rps.goldloan.dto.UserResponse;
import com.rps.goldloan.security.CustomUserDetails;
import com.rps.goldloan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            LoginResponse loginResponse = new LoginResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getUser().getFullName(),
                userDetails.getUser().getEmail(),
                userDetails.getRole(),
                userDetails.getUser().getBranch() != null ? userDetails.getUser().getBranch().getId() : null,
                "Login successful"
            );

            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setMessage("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            SecurityContextHolder.clearContext();
         
            return ResponseEntity.ok("Logout successful");
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LoginResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setMessage("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        LoginResponse loginResponse = new LoginResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getUser().getFullName(),
            userDetails.getUser().getEmail(),
            userDetails.getRole(),
            userDetails.getUser().getBranch() != null ? userDetails.getUser().getBranch().getId() : null,
            "User authenticated"
        );

        return ResponseEntity.ok(loginResponse);
    }
}

