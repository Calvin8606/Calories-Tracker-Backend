package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.config.JwtProvider;
import com.mycalories.CaloriesTracker.dto.UserDto;
import com.mycalories.CaloriesTracker.exception.UserAlreadyExistsException;
import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.request.LoginRequest;
import com.mycalories.CaloriesTracker.response.AuthResponse;
import com.mycalories.CaloriesTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUserHandler(@RequestBody @Valid UserDto userDto) {
        try {
            User savedUser = userService.registerUser(userDto);

            UserDto responseDto = new UserDto();
            responseDto.setEmail(savedUser.getEmail());
            responseDto.setFirstName(savedUser.getFirstName());
            responseDto.setMiddleName(savedUser.getMiddleName());
            responseDto.setLastName(savedUser.getLastName());
            responseDto.setPhoneNumber(savedUser.getPhoneNumber());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDto
                    .getEmail(), userDto.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = JwtProvider.generateToken(authentication);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Sign Up Success");
            authResponse.setJwt(jwt);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Generate JWT token
        Authentication authentication = userService.getAuthenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        User user = userService.findByEmail(email);
        boolean isProfileComplete = (user.getUserProfile() != null);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Sign In Success");
        authResponse.setJwt(jwt);
        authResponse.setProfileComplete(isProfileComplete);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/details")
    public ResponseEntity<UserDto> getUserDetails(Principal principal) {
        try {
            User user = userService.findByEmail(principal.getName());
            UserDto userDto = new UserDto();
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPhoneNumber(user.getPhoneNumber());
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestBody Map<String, String> request, Principal principal) {
        try {
            String phoneNumber = request.get("phoneNumber");
            userService.updatePhoneNumber(principal.getName(), phoneNumber);
            return new ResponseEntity<>("Phone number updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Phone number update failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request, Principal principal) {
        try {
            String newPassword = request.get("password");
            if (newPassword == null || newPassword.isEmpty()) {
                return new ResponseEntity<>("New password cannot be empty", HttpStatus.BAD_REQUEST);
            }
            userService.updatePassword(principal.getName(), newPassword);
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace for debugging
            return new ResponseEntity<>("Password update failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}