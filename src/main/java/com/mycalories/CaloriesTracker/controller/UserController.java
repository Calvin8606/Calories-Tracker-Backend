//package com.mycalories.CaloriesTracker.controller;
//
//import com.mycalories.CaloriesTracker.config.JwtProvider;
//import com.mycalories.CaloriesTracker.dto.UserDto;
//import com.mycalories.CaloriesTracker.exception.UserAlreadyExistsException;
//import com.mycalories.CaloriesTracker.model.User;
//import com.mycalories.CaloriesTracker.repository.UserRepository;
//import com.mycalories.CaloriesTracker.request.LoginRequest;
//import com.mycalories.CaloriesTracker.response.AuthResponse;
//import com.mycalories.CaloriesTracker.service.CustomUserDetailsImpl;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/user")
//public class UserController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private CustomUserDetailsImpl customUserDetails;
//
//    @PostMapping("/register")
//    public ResponseEntity<User> registerUserHandler(@RequestBody @Valid UserDto userDto) {
//        Optional<User> isUserEmailExist = userRepository.findByEmail(userDto.getEmail());
//        Optional<User> isUserPhoneNumberExist = userRepository.findByPhoneNumber(userDto.getPhoneNumber());
//        if (isUserEmailExist.isPresent()) {
//            throw new UserAlreadyExistsException("Email already exists");
//        }
//
//        if (isUserPhoneNumberExist.isPresent()) {
//            throw new UserAlreadyExistsException("Number already exists");
//        }
//
//        User newUser = new User();
//        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        newUser.setEmail(userDto.getEmail());
//        newUser.setFirstName(userDto.getFirstName());
//        newUser.setMiddleName(userDto.getMiddleName());
//        newUser.setLastName(userDto.getLastName());
//
//        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isEmpty()) {
//            newUser.setPhoneNumber(userDto.getPhoneNumber());
//        }
//
//        User savedUser = userRepository.save(newUser);
//
//        // Convert to response DTO
//        UserDto responseDto = new UserDto();
//        responseDto.setEmail(savedUser.getEmail());
//        responseDto.setFirstName(savedUser.getFirstName());
//        responseDto.setMiddleName(savedUser.getMiddleName());
//        responseDto.setLastName(savedUser.getLastName());
//        responseDto.setPhoneNumber(savedUser.getPhoneNumber());
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDto
//                .getEmail(), userDto.getPassword());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = JwtProvider.generateToken(authentication);
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setMessage("Sign Up Success");
//        authResponse.setJwt(jwt);
//        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
//        String username = loginRequest.getEmail();
//        String password = loginRequest.getPassword();
//
//        Authentication authentication = authenticate(username, password);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = JwtProvider.generateToken(authentication);
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setMessage("Sign In Success");
//        authResponse.setJwt(jwt);
//
//        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
//    }
//
//    private Authentication authenticate(String username, String password) {
//        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
//        if (userDetails == null) {
//            throw new BadCredentialsException("invalid username");
//        }
//        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new BadCredentialsException("invalid username");
//        }
//        return new UsernamePasswordAuthenticationToken(userDetails, null
//                , userDetails.getAuthorities());
//    }
//}

package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.config.JwtProvider;
import com.mycalories.CaloriesTracker.dto.UserDto;
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

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUserHandler(@RequestBody @Valid UserDto userDto) {
        User savedUser = userService.registerUser(userDto);

        // Create a response DTO (if needed)
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
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Generate JWT token
        Authentication authentication = userService.getAuthenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Sign In Success");
        authResponse.setJwt(jwt);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}