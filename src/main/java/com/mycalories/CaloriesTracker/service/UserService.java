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
}