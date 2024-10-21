package com.mycalories.CaloriesTracker.service;

import com.mycalories.CaloriesTracker.dto.UserDto;
import com.mycalories.CaloriesTracker.exception.UserAlreadyExistsException;
import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



//    public User registerUser(UserDto userDto) {
//
//        User user = convertToUserEntity(userDto);

//        logger.info("Registering user with username: {}, email: {}", user.getUsername(), user.getEmail());
//
////        // Check if username already exists
////        if (userRepository.existsByUsername(user.getUsername())) {
////            throw new UserAlreadyExistsException("Username already taken");
////        }
////
////        // Check if email already exists
////        if (userRepository.existsByEmail(user.getEmail())) {
////            throw new UserAlreadyExistsException("Email already in use");
////        }
////
////        // Check if phone number is provided and already exists
////        if (user.getPhoneNumber() != null && userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
////            throw new UserAlreadyExistsException("Phone number already in use");
////        }
////
////        String encodedPassword = passwordEncoder.encode(user.getPassword());
////        user.setPassword(encodedPassword);
////
//            return userRepository.save(user);
//    }

    private User convertToUserEntity(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Remember to hash PW
        return user;
    }



}
