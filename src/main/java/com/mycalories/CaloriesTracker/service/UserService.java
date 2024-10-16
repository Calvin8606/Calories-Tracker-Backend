package com.mycalories.CaloriesTracker.service;

import com.mycalories.CaloriesTracker.exception.UserAlreadyExistsException;
import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        } else if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone Number already in use");
        }
        return userRepository.save(user);
    }

}
