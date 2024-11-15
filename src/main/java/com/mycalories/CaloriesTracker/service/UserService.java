package com.mycalories.CaloriesTracker.service;

import com.mycalories.CaloriesTracker.dto.UserDto;
import com.mycalories.CaloriesTracker.exception.UserAlreadyExistsException;
import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsImpl customUserDetails;

    // Handle user registration
    public User registerUser(UserDto userDto) {
        Optional<User> isUserEmailExist = userRepository.findByEmail(userDto.getEmail());
        Optional<User> isUserPhoneNumberExist = userRepository.findByPhoneNumber(userDto.getPhoneNumber());

        if (isUserEmailExist.isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (isUserPhoneNumberExist.isPresent()) {
            throw new UserAlreadyExistsException("Number already exists");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setEmail(userDto.getEmail());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setMiddleName(userDto.getMiddleName());
        newUser.setLastName(userDto.getLastName());

        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isEmpty()) {
            newUser.setPhoneNumber(userDto.getPhoneNumber());
        }

        return userRepository.save(newUser);
    }

    // Handle user authentication
    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("invalid email");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null
                , userDetails.getAuthorities());
    }

    public Authentication getAuthenticate(String email, String password) {
        return this.authenticate(email, password);
    }

    public User findByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return findUser.get();
    }

    public void updatePhoneNumber(String email, String phoneNumber) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                user.setPhoneNumber(phoneNumber);
                userRepository.save(user);
                System.out.println("Phone number updated successfully for user: " + email);
            } else {
                throw new IllegalArgumentException("Phone number cannot be empty.");
            }
        } catch (Exception e) {
            System.err.println("Error updating phone number for user: " + email);
            e.printStackTrace();
            throw e;
        }
    }

    public void updatePassword(String email, String newPassword) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (newPassword == null || newPassword.isEmpty()) {
                throw new IllegalArgumentException("New password cannot be empty.");
            }

            if (newPassword.length() < 8) {
                throw new IllegalArgumentException("New password must be at least 8 characters long.");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            System.out.println("Password updated successfully for user: " + email);
        } catch (Exception e) {
            System.err.println("Error updating password for user: " + email);
            e.printStackTrace();
            throw e;
        }
    }
}